/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package edu.proj.onlinepdfreader;

import edu.proj.onlinepdfreader.utils.AppDatabase;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author liree
 */
@WebServlet(name = "ReaderServlet", urlPatterns = {"/reader/*"})
public class ReaderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        byte[] content = null;
        boolean valid = true;
        
        if (pathInfo!=null && !pathInfo.equals("/")) {
            boolean pdfExist;
            try {
                String pdfId = pathInfo.substring(pathInfo.length()-36, pathInfo.length());
                pdfExist = !AppDatabase.getInstance().getPdf(pdfId).isEmpty();
            }
            catch (SQLException|StringIndexOutOfBoundsException ex) {
                pdfExist = false;
            }

            String readerTemplateFilePath = String.format("%s/src/frontend/.next/server/app/reader.html", getServletContext().getAttribute("projectDirectory"));
            File readerTemplate = new File(readerTemplateFilePath);
            
            if (readerTemplate.exists() && pdfExist) {
                response.setContentType(getServletContext().getMimeType(readerTemplateFilePath));
                content = FileUtils.readFileToByteArray(readerTemplate);
            }
            else {
                valid = false;
            }
        }
        else {
            valid = false;
        }

        if (valid) {
            response.setContentLength(content.length);
            response.getOutputStream().write(content);
        }
        else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

    }
}
