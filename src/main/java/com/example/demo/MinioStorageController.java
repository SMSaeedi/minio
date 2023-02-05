package com.example.demo;

import io.minio.messages.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MinioStorageController {
    private final MinioService minioService;

    @GetMapping(path = "/buckets")
    public List<Bucket> listBuckets() {
        return minioService.getAllBuckets();
    }

    @PostMapping(path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @SneakyThrows
    public Map<String, String> uploadFile(@RequestPart(value = "file", required = false) MultipartFile file) {
        isValidFileFormat(file);

        var key = minioService.uploadFile(file.getOriginalFilename(), file.getBytes());
        Map<String, String> result = new HashMap<>();
        result.put("key", key);
        result.put("file name", file.getOriginalFilename());

        return result;
    }

    private void isValidFileFormat(MultipartFile file) {
        var validContentTypes = new ArrayList<>();
        validContentTypes.add("jpg");
        validContentTypes.add("png");
        validContentTypes.add("bmp");
        validContentTypes.add("jpeg");
        String[] split = file.getOriginalFilename().split("\\.");
        String suffix = split[split.length - 1];

        if (!validContentTypes.contains(suffix))
            throw new RuntimeException("invalid file type");
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