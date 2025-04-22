package com.a2823kevin.pdfreader.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;


@Configuration
@ConfigurationProperties(prefix = "external.pdf2html-server")
@Data
public class Pdf2htmlServerProperties {
    private String url;
}
