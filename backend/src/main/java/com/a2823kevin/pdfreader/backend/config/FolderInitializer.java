package com.a2823kevin.pdfreader.backend.config;

import java.io.File;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FolderInitializer implements CommandLineRunner {
    private final FileSavingProperties fileSavingProperties;

    @Override
    public void run(String... args) throws Exception {
        File folder = new File(fileSavingProperties.getHtmlpath());
        if (!folder.exists()) {
            folder.mkdirs();
        }

        folder = new File(fileSavingProperties.getPdfpath());
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }
}
