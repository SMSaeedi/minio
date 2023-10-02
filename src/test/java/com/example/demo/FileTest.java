package com.example.demo;

import io.minio.MinioClient;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.InputStream;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

@SpringBootTest
class FileTest {
    @Autowired
    MinioService fileService;
    private static MinioClient minioClient;

    @BeforeEach
    void init() {
        minioClient = mock(MinioClient.class);
        ReflectionTestUtils.setField(fileService, "minioClient", minioClient);
    }

    @Test
    @SneakyThrows
    public void uploadFile() {
//        doNothing().when(minioClient).putObject(bucketName, anyString(), any(InputStream.class), anyInt(), "application/octet-stream");
        doNothing().when(minioClient).putObject(anyString(), anyString(), any(InputStream.class), anyInt(), anyString());
    }
}