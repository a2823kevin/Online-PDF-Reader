/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/ServletListener.java to edit this template
 */
package edu.proj.onlinepdfreader.listeners;

import edu.proj.onlinepdfreader.utils.AppClients;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Web application lifecycle listener.
 *
 * @author liree
 */
@WebListener
public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent evt) {}

    @Override
    public void sessionDestroyed(HttpSessionEvent evt) {
        AppClients.getInstance().removeClient(evt.getSession().getId());
    }
}
