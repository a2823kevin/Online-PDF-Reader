package com.a2823kevin.pdfreader.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "storage")
@Data
public class FileSavingProperties {
    private String pdfpath;
    // private String htmlpath;
}
