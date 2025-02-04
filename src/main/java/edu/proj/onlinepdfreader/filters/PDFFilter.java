/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Filter.java to edit this template
 */
package edu.proj.onlinepdfreader.filters;

import edu.proj.onlinepdfreader.utils.AppDatabase;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author liree
 */
@WebFilter(filterName = "PDFFilter", urlPatterns = {"/pdf/*", "/pdfFile/*", "/pdfs"})
public class PDFFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        boolean valid = true;
        
        if (!req.getRequestURI().startsWith("/OnlinePDFReader/pdfs") && !req.getMethod().equals("POST")) {
            String pathInfo = req.getPathInfo();
            String pdfId = pathInfo.substring(pathInfo.length()-36, pathInfo.length());
            
            try {
                if (AppDatabase.getInstance().getPdf(pdfId).isEmpty()) {
                    valid = false;
                }
            }
            catch (SQLException ex) {
                valid = false;
            }
        }
        
        if (valid) {
            chain.doFilter(request, response);
        }
        else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    public void destroy() {}
}
