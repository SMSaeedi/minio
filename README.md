Minio is API compatible with Amazon S3 cloud storage service.
------------------------------
It has two diff ways authentications:
  user/pass and Amazon web token

-----------------------------------------------------------------------------------------

You can reach the upload and download file through the below links: 

  http://localhost:8181/upload
------------------------------
  http://localhost:8181/download?file=fileName
------------------------------

------------------------------------------------------------------------------------------

Docker image:
------------------------------
  sudo apt install podman
------------------------------
  podman run -p 9000:9000 -p 9001:9001 \ quay.io/minio/minio server /data --console-address ":9001"
------------------------------------------------------------------------------------------
