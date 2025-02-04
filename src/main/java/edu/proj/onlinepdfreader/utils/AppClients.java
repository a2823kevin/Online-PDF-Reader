/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.proj.onlinepdfreader.utils;

import java.util.HashMap;

/**
 *
 * @author liree
 */
public class AppClients {
    private static AppClients instance;
    private static HashMap<String, ClientInfo> clients;
    
    private AppClients() {
        clients = new HashMap<String, ClientInfo>();
    }
    
    public static AppClients getInstance() {
        if (instance == null) {
            instance = new AppClients();
        }
        return instance;
    }
    
    public void addClient(String sessionId, String clientId) {
        clients.put(sessionId, new ClientInfo(clientId));
    }
    
    public ClientInfo getClient(String sessionId) {
        if (clients.containsKey(sessionId)) {
            return clients.get(sessionId);
        }
        return null;
    }
    
    public void removeClient(String sessionId) {
        if (clients.containsKey(sessionId)) {
            clients.remove(sessionId);
        }
    }
    
    public class ClientInfo {
        private String id;
        public ClientInfo(String accountId) {
            setId(accountId);
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}

