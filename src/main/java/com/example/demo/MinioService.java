package com.example.demo;

import io.minio.MinioClient;
import io.minio.messages.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MinioService {
    private final MinioClient minioClient;

    @Value("${minio.bucket.name}")
    String defaultBucketName;

    @Value("${minio.default.folder}")
    String defaultBaseFolder;

    public List<Bucket> getAllBuckets() {
        try {
            return minioClient.listBuckets();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @SneakyThrows
    public String uploadFile(String name, byte[] content) {
        String key = UUID.randomUUID() + "_" + name;

        InputStream inputStream = new ByteArrayInputStream(content);

        minioClient.putObject(defaultBucketName, key, inputStream, content.length, "application/octet-stream");

        return key;
    }

    @SneakyThrows
    public byte[] downloadFile(String key) {
        InputStream obj = minioClient.getObject(defaultBucketName, key);

        byte[] content = IOUtils.toByteArray(obj);
        obj.close();

        return content;
    }

 /*   public static void main(String[] args) throws IOException {
        ClassLoader classLoader = MinioService.class.getClassLoader();
        File file = new File(classLoader.getResource("Screenshot.png").getFile());
        byte[] fileContent = Files.readAllBytes(file.toPath());
        System.out.println(fileContent);
    }*/
}