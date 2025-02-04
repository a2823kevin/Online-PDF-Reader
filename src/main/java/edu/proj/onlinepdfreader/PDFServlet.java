package edu.proj.onlinepdfreader;

import edu.proj.onlinepdfreader.utils.ConverterClient;
import edu.proj.onlinepdfreader.utils.AppDatabase;
import edu.proj.onlinepdfreader.utils.AppClients;
import edu.proj.onlinepdfreader.utils.AppClients.ClientInfo;
import com.google.gson.Gson;
import edu.proj.onlinepdfreader.utils.AppDatabase.PdfInfo;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

@WebServlet(name = "PDFServlet", urlPatterns = {"/pdf/*", "/pdfFile/*", "/pdfs"})
@MultipartConfig
public class PDFServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // get all pdfs
        if (request.getRequestURI().startsWith("/OnlinePDFReader/pdfs")) {
            handleGetPdfs(request, response);
        }
        
        else {
            if (request.getRequestURI().startsWith("/OnlinePDFReader/pdf/file")) {
                // get pdf file
                handleGetPdf(request, response);
            }

            else if (request.getRequestURI().startsWith("/OnlinePDFReader/pdf/thumbnail")) {
                // get pdf thumbnail
                handleGetPdfThumbnail(request, response);
            }

            else {
                // get converted html
                handleGetHtml(request, response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HashMap<String, String> resp = null;
        for (Part part: request.getParts()) {
            // save file
            String wd = (String) getServletContext().getAttribute("projectDirectory");
            String pdfStoragePath = String.format("%s/src/assets/pdf", wd);
            File pdfFile = new File(String.format("%s/%s", pdfStoragePath, part.getSubmittedFileName()));
            FileUtils.copyInputStreamToFile(part.getInputStream(), pdfFile);

            // upload pdf
            resp = new ConverterClient(wd).uploadPDF(pdfFile);
        }
        response.getWriter().write(new Gson().toJson(resp));
        
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pdfId = request.getPathInfo().substring(1);
        ClientInfo info = AppClients.getInstance().getClient(request.getSession().getId());
        if (info!=null) {
            HashMap<String, Double> reqContent = new Gson().fromJson(IOUtils.toString(request.getInputStream(), "utf-8"), HashMap.class);
            try {
                AppDatabase.getInstance().changePageNum(pdfId, info.getId(), reqContent.get("atPage").intValue());
            }
            catch (SQLException ex) {}
        }
    }
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pdfId = request.getPathInfo().substring(1);
        ClientInfo info = AppClients.getInstance().getClient(request.getSession().getId());
        if (info!=null) {
            try {
                AppDatabase.getInstance().deletePDF(pdfId, info.getId());
            }
            catch (SQLException ex) {}
        }
    }
    
    private void handleGetPdfs(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<PdfInfo> result = null;
        ClientInfo info = AppClients.getInstance().getClient(request.getSession().getId());
        if (info!=null) {
            try {
                result = AppDatabase.getInstance().getPdfs(info.getId());
            }
            catch (SQLException ex) {
                result = new ArrayList<>();
            }
        }
        response.getWriter().write(new Gson().toJson(result));
    }

    private void handleGetPdf(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pdfId = request.getPathInfo().substring(6);
        try {
            String pdfFilePath = AppDatabase.getInstance().getPdf(pdfId).get("pdfPath");
            File file = new File(pdfFilePath);
            response.setContentType(getServletContext().getMimeType(pdfFilePath));
            response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", Paths.get(pdfFilePath).getFileName().toString()));
            byte[] content = FileUtils.readFileToByteArray(file);
            response.setContentLength(content.length);
            response.getOutputStream().write(content);
        }
        catch (SQLException ex) {
            Logger.getLogger(PDFServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void handleGetPdfThumbnail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pdfId = request.getPathInfo().substring(11);
        try {
            String pdfFilePath = AppDatabase.getInstance().getPdf(pdfId).get("pdfPath");
            File file = new File(pdfFilePath);
            response.setContentType("image/png");
            ImageIO.write(generatePdfThumbnail(file), "png", response.getOutputStream());
        }
        catch (SQLException ex) {
            Logger.getLogger(PDFServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void handleGetHtml(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pdfId = request.getPathInfo().substring(1);
        HashMap<String, String> resp = new HashMap<>();
        try {
            HashMap<String, String> pdf = AppDatabase.getInstance().getPdf(pdfId);
            FileInputStream fis = new FileInputStream(new File(pdf.get("htmlPath")));
            resp.put("htmlText", IOUtils.toString(fis, "utf-8"));
            resp.put("atPage", pdf.get("atPage"));
            fis.close();
        }
        catch (SQLException ex) {}
        response.setHeader("Content-Type", "text/html;charset=utf-8");
        response.getWriter().write(new Gson().toJson(resp));
    }
    
    private BufferedImage generatePdfThumbnail(File pdfFile) {
        try {
            PDDocument doc = Loader.loadPDF(pdfFile);
            PDFRenderer pdfRenderer = new PDFRenderer(doc);
            return pdfRenderer.renderImageWithDPI(0, 100.0f);
        }
        catch (IOException ex) {
            Logger.getLogger(PDFServlet.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}