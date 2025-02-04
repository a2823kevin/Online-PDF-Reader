/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package edu.proj.onlinepdfreader;

import edu.proj.onlinepdfreader.utils.ConverterClient;
import edu.proj.onlinepdfreader.utils.AppDatabase;
import edu.proj.onlinepdfreader.utils.AppClients;
import com.google.gson.Gson;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import javax.mail.internet.ParseException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author liree
 */
@WebServlet(name = "UploadTaskServlet", urlPatterns = {"/task/*"})
public class UploadTaskServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String wd = (String) getServletContext().getAttribute("projectDirectory");
        ConverterClient converterClient = new ConverterClient(wd);

        // get conversion state
        String token = request.getPathInfo().substring(1);
        HashMap<String, String> state = converterClient.getConversionState(token);
        HashMap<String, String> resp = new HashMap<>();
        switch (state.get("state")) {
            case "working":
                resp.put("state", "working");
                resp.put("progress", state.get("progress"));
                break;

            case "finished":
                // save html
                String pdfId = null;
                try {
                    String fileName = converterClient.saveHtmlFile(token, String.format("%s/src/assets/html", wd));
                    pdfId = AppDatabase.getInstance().addPDF(AppClients.getInstance().getClient(request.getSession().getId()).getId(), fileName);
                }
                catch (ParseException|SQLException|NullPointerException ex) {
                    System.out.println("download failed");
                }
                resp.put("state", "finished");
                resp.put("pdf_id", pdfId);
                break;

            case "failed":
                resp.put("state", "failed");
                break;

            default:
                throw new AssertionError();
        }

        response.getWriter().write(new Gson().toJson(resp));
    }
}
