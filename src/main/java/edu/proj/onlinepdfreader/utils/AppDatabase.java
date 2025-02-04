/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.proj.onlinepdfreader.utils;

import io.github.cdimascio.dotenv.Dotenv;
import java.io.File;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author liree
 */
public class AppDatabase {
    private static AppDatabase instance;
    private String cwd;
    private Dotenv dotenv;
    private Connection connection;

    // init just once (singleton)
    private AppDatabase(String cwd) throws ClassNotFoundException, SQLException {
        this.cwd = cwd;
        dotenv = Dotenv.configure().directory(cwd).load();

        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s/src/assets/app.db", cwd));
        initTables();
    }

    // get the instance of database
    public static AppDatabase getInstance(String cwd) {
        if (instance == null) {
            try {
                instance = new AppDatabase(cwd);
            }
            catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(AppDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return instance;
    }
    
    public static AppDatabase getInstance() {
        return instance;
    }
    
    // initialize
    public void initTables() throws SQLException {
        initAccountTable();
        initPdfTable();
    }

    public void initAccountTable() throws SQLException {
        Statement stmt = connection.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS Account(" +
                     "id TEXT, " +
                     "password TEXT, " +
                     "PRIMARY KEY (id)" +
                     ");";
        stmt.execute(sql);
        stmt.close();
    }
    
    public void initPdfTable() throws SQLException {
        Statement stmt = connection.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS PDF(" +
                     "id TEXT, " +
                     "owner_id TEXT, " +
                     "pdf_path TEXT, " +
                     "html_path TEXT, " +
                     "at_page INTEGER, " +
                     "PRIMARY KEY (id), " +
                     "FOREIGN KEY(owner_id) REFERENCES Account(id)" +
                     ");";
        stmt.execute(sql);
        stmt.close();
    }
    
    // create
    public int addAcount(String id, String password) throws SQLException, NoSuchAlgorithmException {
        if (!getAccount(id).isEmpty()) {
            return 0;
        }
        PreparedStatement pstmt = connection.prepareStatement("INSERT INTO Account VALUES (?, ?);");
        pstmt.setString(1, id);
        pstmt.setString(2, sha256Hashing(password));

        pstmt.execute();
        return 1;
    }

    public String addPDF(String ownerId, String fileName) throws SQLException {
        String pdfPath = String.format("%s/src/assets/pdf/%s.pdf", cwd, fileName);
        String htmlPath = String.format("%s/src/assets/html/%s.html", cwd, fileName);
        if (!new File(pdfPath).exists() || !new File(htmlPath).exists()) {
            return "error";
        }

        String id = UUID.randomUUID().toString();
        PreparedStatement pstmt = connection.prepareStatement("INSERT INTO PDF VALUES (?, ?, ?, ?, 0);");
        pstmt.setString(1, id);
        pstmt.setString(2, ownerId);
        pstmt.setString(3, pdfPath);
        pstmt.setString(4, htmlPath);
        pstmt.execute();
        return id;
    }
    
    // read
    public HashMap<String, String> getAccount(String id) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM Account WHERE id=?;");
        pstmt.setString(1, id);
        
        HashMap<String, String> result = new HashMap();
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            result.put("id", rs.getString("id"));
            result.put("password", rs.getString("password"));
        }
        return result;
    }

    public HashMap<String, String> getPdf(String id) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM PDF WHERE id=?;");
        pstmt.setString(1, id);
        
        HashMap<String, String> result = new HashMap<>();
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            result.put("ownerId", rs.getString("owner_id"));
            result.put("pdfPath", rs.getString("pdf_path"));
            result.put("htmlPath", rs.getString("html_path"));
            result.put("atPage", Integer.toString(rs.getInt("at_page")));
        }
        return result;
    }

    public List<PdfInfo> getPdfs(String ownerId) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("SELECT id, pdf_path, at_page FROM PDF WHERE owner_id=?;");
        pstmt.setString(1, ownerId);
        
        List<PdfInfo> pdfs = new ArrayList<>();
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            PdfInfo pdf = new PdfInfo(rs.getString("id"), rs.getString("pdf_path"), rs.getInt("at_page"));
            pdfs.add(pdf);
        }
        return pdfs;
    }
    
    // update
    public void changePageNum(String pdfId, String clientId, int pageNum) throws SQLException {
        if (getPdf(pdfId).get("ownerId").equals(clientId)) {
            PreparedStatement pstmt = connection.prepareStatement("UPDATE PDF SET at_page=? WHERE id=?");
            pstmt.setInt(1, pageNum);
            pstmt.setString(2, pdfId);
            pstmt.executeUpdate();
        }
    }
    
    // delete
    public void deletePDF(String pdfId, String clientId) throws SQLException {
        HashMap<String, String> pdf = getPdf(pdfId);
        if (pdf.get("ownerId").equals(clientId)) {
            PreparedStatement pstmt = connection.prepareStatement("DELETE FROM PDF WHERE id=?");
            pstmt.setString(1, pdfId);
            pstmt.executeUpdate();
            new File(pdf.get("pdfPath")).delete();
            new File(pdf.get("htmlPath")).delete();
        }
    }
    
    private String sha256Hashing(String password) throws NoSuchAlgorithmException {
        // generate salt
        byte[] salt = new byte[Integer.parseInt(dotenv.get("SALT_LENGTH"))];
        new SecureRandom().nextBytes(salt);

        // password & salt hashing
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(salt);
        byte[] hash = digest.digest(password.getBytes());

        // comebine salt & password
        byte[] saltedHash = new byte[salt.length + hash.length];
        System.arraycopy(salt, 0, saltedHash, 0, salt.length);
        System.arraycopy(hash, 0, saltedHash, salt.length, hash.length);

        // result
        return Base64.getEncoder().encodeToString(saltedHash);
    }
    
    public boolean verifyPassword(String id, String userInputPassword) throws NoSuchAlgorithmException {
        try {
            HashMap<String, String> account = getAccount(id);
            if (account.isEmpty()) {
                return false;
            }
            // get stored salted password
            String saltedPassword = account.get("password");
            byte[] saltedHash = Base64.getDecoder().decode(saltedPassword);
            
            // extract the salt and the hashed password
            int saltLength = Integer.parseInt(dotenv.get("SALT_LENGTH"));
            byte[] salt = new byte[saltLength];
            System.arraycopy(saltedHash, 0, salt, 0, saltLength);
            byte[] hashedPassword = new byte[saltedHash.length-saltLength];
            System.arraycopy(saltedHash, saltLength, hashedPassword, 0, hashedPassword.length);
            
            // verification
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt);
            byte[] hash = digest.digest(userInputPassword.getBytes());
            return MessageDigest.isEqual(hash, hashedPassword);
        } catch (SQLException ex) {
            return false;
        }
    }

    public void exit() throws SQLException {
        connection.close();
    }
    
    public class PdfInfo {
        private String id, name;
        private int atPage;

        public PdfInfo(String id, String pdfPath, int atPage) {
            this.id = id;
            name = FilenameUtils.removeExtension(Paths.get(pdfPath).getFileName().toString());
            this.atPage = atPage;
        }
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try {
            AppDatabase db = AppDatabase.getInstance(".");
            db.addAcount("id1", "password1");

            // correct case
            System.out.println(db.verifyPassword("id1", "password1"));
            // incorrect case
            System.out.println(db.verifyPassword("id1", "password2"));
            
            db.exit();
        }
        catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AppDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}