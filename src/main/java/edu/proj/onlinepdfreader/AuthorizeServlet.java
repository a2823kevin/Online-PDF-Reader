/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package edu.proj.onlinepdfreader;

import edu.proj.onlinepdfreader.utils.AppDatabase;
import edu.proj.onlinepdfreader.utils.AppClients;
import edu.proj.onlinepdfreader.utils.AppClients.ClientInfo;
import com.google.gson.Gson;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author liree
 */
@WebServlet(name = "AuthServlet", urlPatterns = { "/auth/*" })
public class AuthorizeServlet extends HttpServlet {
    // get client id from session
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sessionId = request.getSession().getId();
        HashMap<String, String> resp = new HashMap<>();
        ClientInfo client = AppClients.getInstance().getClient(sessionId);
        if (client!=null) {
            resp.put("id", client.getId());
        } else {
            resp.put("id", "-1");
        }
        response.getWriter().write(new Gson().toJson(resp));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AppDatabase db = AppDatabase.getInstance();
        HashMap<String, String> resp = new HashMap<>();

        switch (request.getPathInfo()) {
            case "/login":
                handleLogin(request, response, db, resp);
                break;

            case "/logout":
                handleLogout(request);
                break;

            case "/signup":
                handleSignup(request, response, db, resp);
                break;

            default:
                throw new AssertionError();
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response, AppDatabase db, HashMap<String, String> resp) throws IOException {
        HashMap<String, String> reqContent;
        reqContent = new Gson().fromJson(IOUtils.toString(request.getInputStream(), "utf-8"), HashMap.class);
        try {
            boolean passwordCorrect = db.verifyPassword(reqContent.get("id"), reqContent.get("password"));
            resp.put("status", passwordCorrect ? "Success" : "Failed");
            if (passwordCorrect) {
                AppClients.getInstance().addClient(request.getSession().getId(), reqContent.get("id"));
            }
        }
        catch (NoSuchAlgorithmException ex) {
            resp.put("status", "Failed");
        }
        response.getWriter().write(new Gson().toJson(resp));
    }

    private void handleLogout(HttpServletRequest request) throws IOException {
        AppClients.getInstance().removeClient(request.getSession().getId());
    }

    private void handleSignup(HttpServletRequest request, HttpServletResponse response, AppDatabase db, HashMap<String, String> resp) throws IOException {
        HashMap<String, String> reqContent;
        reqContent = new Gson().fromJson(IOUtils.toString(request.getInputStream(), "utf-8"), HashMap.class);
        try {
            int result = db.addAcount(reqContent.get("id"), reqContent.get("password"));
            if (result==1) {
                resp.put("status", "Success");
                AppClients.getInstance().addClient(request.getSession().getId(), reqContent.get("id"));
            }
            else {
                resp.put("status", "Failed");
            }
        }
        catch (SQLException | NoSuchAlgorithmException ex) {
            resp.put("status", "Failed");
        }
        response.getWriter().write(new Gson().toJson(resp));
    }
}
