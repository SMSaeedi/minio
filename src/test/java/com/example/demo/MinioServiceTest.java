package com.example.demo;

import io.minio.MinioClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MinioServiceTest {
    @Mock
    private MinioClient minioClient;

    private MinioService service;

    @BeforeEach
    void setUp() {
        service = new MinioService(minioClient);
        ReflectionTestUtils.setField(service, "defaultBucketName", "testbucket");
    }

    @Test
    void uploadsContentAndReturnsGeneratedKey() throws Exception {
        var content = "hello".getBytes();

        var key = service.uploadFile("photo.PNG", content);

        assertThat(key).endsWith("_photo.PNG");
        verify(minioClient).putObject(eq("testbucket"), eq(key), any(ByteArrayInputStream.class),
                eq((long) content.length), eq("application/octet-stream"));
    }

    @Test
    void downloadsAndClosesObjectStream() throws Exception {
        var object = spy(new ByteArrayInputStream("hello".getBytes()));
        when(minioClient.getObject("testbucket", "photo.jpg")).thenReturn(object);

        assertThat(service.downloadFile("photo.jpg")).isEqualTo("hello".getBytes());

        verify(object).close();
    }

    @Test
    void wrapsMinioFailures() throws Exception {
        when(minioClient.getObject("testbucket", "missing.jpg")).thenThrow(new RuntimeException("unavailable"));

        assertThatThrownBy(() -> service.downloadFile("missing.jpg"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Unable to download file")
                .hasCauseInstanceOf(RuntimeException.class);
    }

    @Test
    void listsBuckets() throws Exception {
        when(minioClient.listBuckets()).thenReturn(List.of());

        assertThat(service.getAllBuckets()).isEmpty();
    }
}
