package com.example.demo;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {
    @Value("${minio.access.name}")
    String accessKey;
    @Value("${minio.access.secret}")
    String accessSecret;
    @Value("${minio.url}")
    String minioUrl;

    @Bean
    public MinioClient generateMinioClient() {
        try {
            return new MinioClient(minioUrl, accessKey, accessSecret);
        } catch (Exception e) {
            e.getStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}