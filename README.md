# MinIO Service

This is the README file for MinIO is a High-Performance Object Storage system released under General Public License
v3.0.
It is API compatible with the Amazon S3 cloud storage service, this service has two-way authentication,
accessName and secret or Amazon web token.

#### Main Configurations

- **Description**:
    - `minio.bucket.name`: A bucket is similar to a folder or directory in a filesystem, where each bucket can hold an
      arbitrary number of objects.
    - `minio.default.folder`: Create object ends with '/' (also called as folder or directory).
    - `minio.access.name`: Access name is a Username to access the bucket list.
    - `minio.access.secret`: Secret is a Password to access the bucket list.

## Starting the Application

### 1. Setting Environment Variables and Running Main Function in StartMinIOApplication.java

- Set the required environment
  variables (`SPRING_APPLICATION_NAME`, `MINIO_ACCESS_NAME`, `MINIO_PASSWORD`, `MINIO_URL`)
  according to your environment and requirements.
- Run the `main` function in the `StartMinIOApplication.java` file to start the Spring Boot application.

## Notes

- Ensure to MinIO is set up on docket accordingly.
- Refer to the Spring Boot documentation for more information on configuring applications using environment variables.

```bash
docker sudo apt install podman
docker podman run -p 9000:9000 -p 9001:9001 \ quay.io/minio/minio server /data --console-address ":9001"
```

## URLs

- http://localhost:8181/upload
- http://localhost:8181/download?file=fileName