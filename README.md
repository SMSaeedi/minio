# MinIO Service

A Spring Boot REST service for uploading, downloading, and listing objects in MinIO.

## Requirements

- JDK 21
- Maven 3.9+
- MinIO server

## Run MinIO locally

Using Docker:

```bash
docker run --name minio \
  -p 9000:9000 -p 9001:9001 \
  -e MINIO_ROOT_USER=minioadmin \
  -e MINIO_ROOT_PASSWORD=minioadmin \
  quay.io/minio/minio server /data --console-address ":9001"
```

MinIO API: <http://localhost:9000>

MinIO console: <http://localhost:9001>

## Configuration

The default configuration is in `src/main/resources/application.properties`.
Override values with Spring properties or environment-specific configuration:

| Property | Default | Description |
| --- | --- | --- |
| `server.port` | `8181` | Application port |
| `minio.bucket.name` | `testbucket` | Bucket used for file operations |
| `minio.access.name` | `minioadmin` | MinIO access key |
| `minio.access.secret` | `minioadmin` | MinIO secret key |
| `minio.url` | `http://127.0.0.1:9000` | MinIO API URL |

## Run the application

```bash
mvn spring-boot:run
```

Or run the packaged application:

```bash
mvn package
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

## API

### List buckets

```http
GET http://localhost:8181/buckets
```

### Upload a file

```bash
curl -X POST http://localhost:8181/upload \
  -F "file=@photo.png"
```

Supported extensions: `.jpg`, `.jpeg`, `.png`, and `.bmp` (case-insensitive).

The response contains the generated object key and original file name.

### Download a file

```bash
curl -OJ "http://localhost:8181/download?file=<generated-key>"
```

## Tests

Run the unit tests with:

```bash
mvn test
```

The tests cover upload and download behavior, file validation, MinIO failures,
bucket listing, and input stream cleanup. Tests use mocks and do not require a
running MinIO server.