package jforgame.admin.oss;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveBucketArgs;
import io.minio.RemoveObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class MinIoClientProxy {


    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioProperties minioProperties;

    @Autowired
    private OssService ossService;

    public void createBucket(String name) throws IOException {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(name).build());
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public void removeBucket(String name) throws IOException {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(name).build());
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public boolean containsBucket(String name) throws IOException {
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(name).build());
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public String upload(InputStream input, String filePath, String contentType) throws IOException {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioProperties.getBucketName())
                            .object(filePath).stream(input, input.available(), -1)
                            .contentType(contentType)
                            .build());
            return ossService.fullOssPath(filePath);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public InputStream InputStream(String fileName) throws IOException {
        GetObjectArgs request = GetObjectArgs.builder().bucket(minioProperties.getBucketName()).object(fileName).build();
        try {
            return minioClient.getObject(request);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public void remove(String objectName) throws IOException {
        RemoveObjectArgs request = RemoveObjectArgs.builder().bucket(minioProperties.getBucketName()).object(objectName).build();
        try {
            minioClient.removeObject(request);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

}