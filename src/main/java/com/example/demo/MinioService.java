package com.example.demo;

import io.minio.MinioClient;
import io.minio.messages.Bucket;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.UUID;

@Service
public class MinioService {
    private final MinioClient minioClient;

    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Value("${minio.bucket.name}")
    String defaultBucketName;

    @Value("${minio.default.folder}")
    String defaultBaseFolder;

    public List<Bucket> getAllBuckets() {
        try {
            return minioClient.listBuckets();
        } catch (Exception e) {
            throw new IllegalStateException("Unable to list MinIO buckets", e);
        }
    }

    public String uploadFile(String name, byte[] content) {
        String key = UUID.randomUUID() + "_" + name;

        try (var inputStream = new ByteArrayInputStream(content)) {
            minioClient.putObject(defaultBucketName, key, inputStream, content.length, "application/octet-stream");
        } catch (Exception e) {
            throw new IllegalStateException("Unable to upload file", e);
        }

        return key;
    }

    public byte[] downloadFile(String key) {
        try (var object = minioClient.getObject(defaultBucketName, key)) {
            return IOUtils.toByteArray(object);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to download file", e);
        }
    }
}