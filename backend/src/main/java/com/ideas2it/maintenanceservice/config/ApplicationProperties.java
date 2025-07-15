package com.ideas2it.maintenanceservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for the application.
 * Author: AI Assistant, Version: 1.0, Date: 2024-05-01
 */
@Data
@Component
@ConfigurationProperties(prefix = "app")
public class ApplicationProperties {

    private Jwt jwt = new Jwt();
    private FileUpload fileUpload = new FileUpload();
    private Aws aws = new Aws();

    @Data
    public static class Jwt {
        private String secret;
        private long expiration;
    }

    @Data
    public static class FileUpload {
        private long maxSize;
        private String allowedTypes;
    }

    @Data
    public static class Aws {
        private S3 s3 = new S3();
        private String accessKeyId;
        private String secretAccessKey;
        private String region;

        @Data
        public static class S3 {
            private String bucket;
        }
    }
} 