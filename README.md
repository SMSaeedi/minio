minio is API compatible with Amazon S3 cloud storage service.
You can reach the upload and download file through the below links: 
  http://localhost:8181/upload 
  http://localhost:8181/download?file=fileName
  
  
Minio docker image:
  sudo apt install podman
  podman run -p 9000:9000 -p 9001:9001 \
  quay.io/minio/minio server /data --console-address ":9001"
