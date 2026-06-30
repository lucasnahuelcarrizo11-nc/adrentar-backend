package com.example.adrentar.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "docusign")
@Data
public class DocuSignConfig {

    private String integrationKey;
    private String userId;
    private String accountId;
    private String basePath;
    private String oauthBasePath;
    private String privateKeyPath;
    private String publicKeyPath;
}
