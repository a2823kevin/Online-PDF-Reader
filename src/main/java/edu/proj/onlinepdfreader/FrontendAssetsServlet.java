/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package edu.proj.onlinepdfreader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
@WebServlet(name = "FrontendAssetsServlet", urlPatterns = {"/site/*"})
public class FrontendAssetsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getPathInfo().startsWith("/_next")) {
            String filePath = String.format("%s/src/frontend/.%s", getServletContext().getAttribute("projectDirectory"), request.getPathInfo().substring(2));
            
            boolean isAcceptedFile = true;
            for (String excludedFileName:(ArrayList<String>)getServletContext().getAttribute("excludedFiles")) {
                if (filePath.endsWith(excludedFileName)) {
                    isAcceptedFile = false;
                    break;
                }
            }

            File file = new File(filePath);
            if (file.exists() && isAcceptedFile) {
                response.setContentType(getServletContext().getMimeType(filePath));
                byte[] content = FileUtils.readFileToByteArray(file);
                response.setContentLength(content.length);
                response.getOutputStream().write(content);
            }
            else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
        else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
