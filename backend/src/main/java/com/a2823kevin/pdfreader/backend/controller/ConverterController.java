package com.a2823kevin.pdfreader.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.a2823kevin.pdfreader.backend.dto.ConversionStateDTO;
import com.a2823kevin.pdfreader.backend.dto.UploadPDFResponseDTO;
import com.a2823kevin.pdfreader.backend.service.ConverterService;

import java.io.IOException;

@RestController
@RequestMapping("/convert")
public class ConverterController {

    private final ConverterService converterService;

    public ConverterController(ConverterService converterService) {
        this.converterService = converterService;
    }

    /**
     * upload pdf
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadPdf(@RequestParam("file") MultipartFile file) throws IOException {
        UploadPDFResponseDTO responseDTO = converterService.uploadPDF(file);
        System.out.println(responseDTO.getMessage());
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Get conversion state
     */
    @GetMapping("/state/{taskId}")
    public ResponseEntity<?> getState(@PathVariable String taskId) throws Exception {
        ConversionStateDTO responseDTO = converterService.getConversionState(taskId);
        System.out.println(responseDTO.getMessage());
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * store html file
     */
    @GetMapping("/download/{taskId}")
    public ResponseEntity<?> downloadHtml(@PathVariable String taskId) throws IOException {
        String response = converterService.saveHtmlFile(taskId);
        System.out.println(response);
        return ResponseEntity.ok(response);
    }
}
