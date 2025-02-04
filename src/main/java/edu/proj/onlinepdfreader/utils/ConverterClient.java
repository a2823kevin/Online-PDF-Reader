/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.proj.onlinepdfreader.utils;

import com.google.gson.Gson;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.mail.internet.ContentDisposition;
import javax.mail.internet.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

// Client to pdf2html server
public class ConverterClient {
    private Dotenv dotenv;
    private CloseableHttpClient client = HttpClientBuilder.create().build();
    
    public ConverterClient(String cwd) {
        dotenv = Dotenv.configure().directory(cwd).load();
    }
    
    public HashMap<String, String> uploadPDF(File pdfFile) throws IOException {
        HttpPost request = new HttpPost(String.format("http://%s:%s/pdf", dotenv.get("PDF2HTML_SERVER_IP"), dotenv.get("PDF2HTML_SERVER_PORT")));
        HttpEntity content = MultipartEntityBuilder.create().addBinaryBody("uploaded_file", pdfFile, ContentType.DEFAULT_BINARY, pdfFile.getName()).build();
        request.setEntity(content);

        HttpResponse response = client.execute(request);
        HashMap<String, String> responseJSON = new Gson().fromJson(EntityUtils.toString(response.getEntity()), HashMap.class);
        return responseJSON;
    }

    public HashMap<String, String> getConversionState(String token) throws IOException {
        HttpGet request = new HttpGet(String.format("http://%s:%s/task/%s", dotenv.get("PDF2HTML_SERVER_IP"), dotenv.get("PDF2HTML_SERVER_PORT"), token));
        HttpResponse response = client.execute(request);
        if (response.getStatusLine().getStatusCode()==HttpStatus.SC_NOT_FOUND) {
            throw new RuntimeException("error requesting");
        }

        return new Gson().fromJson(IOUtils.toString(response.getEntity().getContent(), "utf-8"), HashMap.class);
    }
    
    public String saveHtmlFile(String token, String saveFolderPath) throws IOException, ParseException {
        HttpGet request = new HttpGet(String.format("http://%s:%s/html/%s", dotenv.get("PDF2HTML_SERVER_IP"), dotenv.get("PDF2HTML_SERVER_PORT"), token));
        HttpResponse response = client.execute(request);
        if (response.getStatusLine().getStatusCode()==HttpStatus.SC_NOT_FOUND) {
            return "error";
        }

        String htmlFileName = new ContentDisposition(response.getFirstHeader("Content-Disposition").getValue()).getParameter("filename");
        FileUtils.copyInputStreamToFile(response.getEntity().getContent(), new File(String.format("%s/%s", saveFolderPath, htmlFileName)));
        return FilenameUtils.removeExtension(htmlFileName);
    }

    public static void main(String[] args) throws IOException, ParseException, InterruptedException {
        ConverterClient client = new ConverterClient(".");
        File pdfFile = new File("src/assets/test/dummy.pdf");
        HashMap<String, String> task = client.uploadPDF(pdfFile);
        if (task.get("status").equals("Accepted")) {
            HashMap<String, String> state;
            while (true) {
                state = client.getConversionState(task.get("token"));
                if (!state.get("state").equals("working")) {
                    break;
                }
                Thread.sleep(1000);
            }
            if (state.get("progress").equals("finished")) {
                client.saveHtmlFile(task.get("token"), "src/assets/html");
            }
        }
    }
}