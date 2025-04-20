package com.a2823kevin.pdfreader.backend.service;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.a2823kevin.pdfreader.backend.config.FileSavingProperties;
import com.a2823kevin.pdfreader.backend.config.Pdf2htmlServerProperties;
import com.a2823kevin.pdfreader.backend.dto.ConversionStateDTO;
import com.a2823kevin.pdfreader.backend.dto.UploadPDFResponseDTO;
import com.a2823kevin.pdfreader.backend.model.Book;
import com.a2823kevin.pdfreader.backend.model.UploadPdfStatus;
import com.a2823kevin.pdfreader.backend.repository.BookRepository;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Slf4j
public class ConverterService {
    private final Pdf2htmlServerProperties pdf2htmlServerProperties;
    private final FileSavingProperties fileSavingProperties;

    private final WebClient webClient;
    private final BookRepository bookRepository;

    public ConverterService(Pdf2htmlServerProperties pdf2htmlServerProperties, FileSavingProperties fileSavingProperties, BookRepository bookRepository) {
        this.pdf2htmlServerProperties = pdf2htmlServerProperties;
        this.fileSavingProperties = fileSavingProperties;

        this.webClient = WebClient.builder()
        .baseUrl(this.pdf2htmlServerProperties.getUrl()
        ).exchangeStrategies(ExchangeStrategies.builder()
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024 * 1024))
        .build()).build();
        this.bookRepository = bookRepository;
    }

    /**
     * Uploads a PDF file to the pdf2html server.
     *
     * @param pdfFile The PDF file to upload.
     * @return A DTO containing the server's response.
     */
    public UploadPDFResponseDTO uploadPDF(MultipartFile pdfFile) {
        if (!FilenameUtils.getExtension(pdfFile.getOriginalFilename()).equals("pdf")) {
            return UploadPDFResponseDTO.rejectExtension();
        }

        try {
            // Map the response to DTO
            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder.part("file", pdfFile.getResource())
                    .header("Content-Disposition", "form-data; name=\"file\"; filename=\"" + pdfFile.getOriginalFilename() + "\"");

            UploadPDFResponseDTO response = webClient.post()
                    .uri("/pdf")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(builder.build()))
                    .retrieve()
                    .bodyToMono(UploadPDFResponseDTO.class)
                    .block();

            if (response.getStatus() == UploadPdfStatus.ACCEPTED) {
                // Save pdf file
                Book newBook = new Book();
                String pdfName = response.getTaskId() + ".pdf";

                byte[] fileBytes = pdfFile.getBytes();
                File outputFile = Paths.get(fileSavingProperties.getPdfpath(), pdfName).toFile();
                FileUtils.writeByteArrayToFile(outputFile, fileBytes);

                String bookName = FilenameUtils.getBaseName(pdfFile.getOriginalFilename());
                newBook.setPdfPath(String.format("%s/%s", fileSavingProperties.getPdfpath(), pdfName));
                newBook.setName(bookName);
                bookRepository.save(newBook);
            }
            return response;
        }
        catch (IOException e) {
            log.error("Failed to upload PDF", e);
            return UploadPDFResponseDTO.rejectIO();
        }
    }

    /**
     * Gets the conversion state of a task from the pdf2html server as a DTO.
     *
     * @param taskId The task id.
     * @return A DTO containing the task's state.
     */
    public ConversionStateDTO getConversionState(String taskId) {
        try {
            return webClient.get()
                    .uri("/task/{id}", taskId)
                    .retrieve()
                    .bodyToMono(ConversionStateDTO.class)
                    .block();
        }
        catch (WebClientResponseException e) {
            log.error("Failed to get conversion state", e);
            return ConversionStateDTO.failedNotFound();
        }
    }

    /**
     * Saves the converted HTML file to the specified folder.
     *
     * @param taskId          The task id.
     * @return The path of the saved HTML file.
     */
    public String saveHtmlFile(String taskId) {
        try {
            ResponseEntity<byte[]> response = webClient.get()
                .uri("/html/{id}", taskId)
                .retrieve()
                .toEntity(byte[].class)
                .block();
            if (response == null || response.getBody() == null) {
                return "Error requesting html file: Download failed or empty response.";
            }

            byte[] body = response.getBody();

            // Save html file
            Book book = bookRepository.getByPdfPath(String.format("%s/%s.pdf", fileSavingProperties.getPdfpath(), taskId));
            String htmlName = UUID.randomUUID().toString() + ".html";
            
            File outputFile = Paths.get(fileSavingProperties.getHtmlpath(), htmlName).toFile();
            FileUtils.writeByteArrayToFile(outputFile, body);

            book.setHtmlPath(String.format("%s/%s", fileSavingProperties.getHtmlpath(), htmlName));
            bookRepository.save(book);

            return "HTML saved: " + outputFile.getAbsolutePath();
        }
        catch (WebClientResponseException e) {
            log.error("Failed to save HTML file", e);
            return "Error requesting html file: task not found";
        }
        catch (IOException e) {
            log.error("Failed to save HTML file", e);
            return "Error requesting html file: IOException";
        }
    }
}