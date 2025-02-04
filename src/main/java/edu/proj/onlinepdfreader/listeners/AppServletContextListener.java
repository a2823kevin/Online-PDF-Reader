/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.proj.onlinepdfreader.listeners;

import edu.proj.onlinepdfreader.utils.AppClients;
import edu.proj.onlinepdfreader.utils.AppDatabase;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 *
 * @author liree
 */
@WebListener
public class AppServletContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent evt) {
        ServletContext ctx = evt.getServletContext();

        // project directory
        String cwd = Paths.get(ctx.getRealPath("/")).getParent().getParent().toString().replace("\\", "/");
        ctx.setAttribute("projectDirectory", cwd);
        
        // init db & clients map
        AppDatabase.getInstance(cwd);
        AppClients.getInstance();

        // excluded request file
        List<String> excludedFiles = new ArrayList<>();
        excludedFiles.add("index.html");
        excludedFiles.add("reader.html");
        ctx.setAttribute("excludedFiles", excludedFiles);
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent evt) {
        try {
            AppDatabase.getInstance().exit();
        }
        catch (SQLException ex) {
            Logger.getLogger(AppServletContextListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
