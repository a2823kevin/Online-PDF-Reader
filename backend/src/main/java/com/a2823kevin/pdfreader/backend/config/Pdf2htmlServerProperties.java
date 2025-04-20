package com.a2823kevin.pdfreader.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "external.pdf2html-server")
@Getter
@Setter
public class Pdf2htmlServerProperties {
    private String url;
}
