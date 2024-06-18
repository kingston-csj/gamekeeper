#!/bin/bash
mkdir mioio && cd minio
mkdir config
mkdir data
docker pull minio/minio
docker run -d  -p 9000:9000 -p 9001:9001 --name minio -v ./data:/data -v ./config:/root/.minio -e "MINIO_ROOT_USER=minio" -e "MINIO_ROOT_PASSWORD=minio123456" minio/minio server /data --console-address ":9001"