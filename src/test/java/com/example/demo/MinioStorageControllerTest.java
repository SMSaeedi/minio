package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mock.web.MockMultipartFile;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MinioStorageControllerTest {
    @Mock
    private MinioService minioService;

    private MinioStorageController controller;

    @BeforeEach
    void setUp() {
        controller = new MinioStorageController(minioService);
    }

    @Test
    void uploadsSupportedExtensionCaseInsensitively() throws Exception {
        var file = new MockMultipartFile("file", "photo.PNG", "image/png", "data".getBytes());
        when(minioService.uploadFile(eq("photo.PNG"), any(byte[].class))).thenReturn("generated-key");

        var result = controller.uploadFile(file);

        assertThat(result).containsEntry("key", "generated-key")
                .containsEntry("file name", "photo.PNG");
    }

    @Test
    void rejectsEmptyFiles() {
        var file = new MockMultipartFile("file", "photo.png", "image/png", new byte[0]);

        assertThatThrownBy(() -> controller.uploadFile(file))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("file must not be empty");
        verifyNoInteractions(minioService);
    }

    @Test
    void rejectsUnsupportedExtensions() {
        var file = new MockMultipartFile("file", "document.pdf", "application/pdf", "data".getBytes());

        assertThatThrownBy(() -> controller.uploadFile(file))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("invalid file type");
        verifyNoInteractions(minioService);
    }

    @Test
    void downloadsFileAsAttachment() {
        when(minioService.downloadFile("photo.jpg")).thenReturn("data".getBytes());

        var response = controller.uploadFile("photo.jpg");

        assertThat(response.getBody()).isInstanceOf(ByteArrayResource.class);
        assertThat(response.getHeaders().getFirst("Content-disposition"))
                .isEqualTo("attachment; filename=\"photo.jpg\"");
        assertThat(response.getBody().getByteArray()).isEqualTo("data".getBytes());
    }
}
