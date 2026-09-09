package com.example.demo;

import io.minio.messages.Bucket;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@RestController
public class MinioStorageController {
    private static final Set<String> VALID_EXTENSIONS = Set.of("jpg", "png", "bmp", "jpeg");

    private final MinioService minioService;

    public MinioStorageController(MinioService minioService) {
        this.minioService = minioService;
    }

    @GetMapping(path = "/buckets")
    public List<Bucket> listBuckets() {
        return minioService.getAllBuckets();
    }

    @PostMapping(path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Map<String, String> uploadFile(@RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        validateFile(file);

        var key = minioService.uploadFile(file.getOriginalFilename(), file.getBytes());
        var result = new HashMap<String, String>();
        result.put("key", key);
        result.put("file name", file.getOriginalFilename());

        return result;
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty() || file.getOriginalFilename() == null) {
            throw new IllegalArgumentException("file must not be empty");
        }

        var fileName = file.getOriginalFilename();
        var extensionStart = fileName.lastIndexOf('.');
        var extension = extensionStart >= 0
                ? fileName.substring(extensionStart + 1).toLowerCase(Locale.ROOT)
                : "";
        if (!VALID_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("invalid file type");
        }
    }

    @GetMapping(path = "/download")
    public ResponseEntity<ByteArrayResource> uploadFile(@RequestParam(value = "file") String file) {
        byte[] data = minioService.downloadFile(file);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + file + "\"")
                .body(resource);
    }
}