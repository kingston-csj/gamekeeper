package jforgame.admin.s3;

import java.io.InputStream;
import java.util.Date;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import jforgame.commons.TimeUtil;

public class AmazonS3Client implements S3Client {

    private AmazonS3 client;

    private OssConfig ossConfig;

    public AmazonS3Client(AmazonS3 client, OssConfig ossConfig) {
        this.client = client;
        this.ossConfig = ossConfig;
    }

    @Override
    public void createBucket(String name) throws OssException {
        CreateBucketRequest createBucketRequest = new CreateBucketRequest(ossConfig.getBucketName());
        try {
            client.createBucket(createBucketRequest);
        } catch (Exception e) {
            throw new OssException(e);
        }
    }

    @Override
    public String upload(InputStream input, String filePath, String contentType) throws OssException {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            if (filePath.endsWith(".gz")) {
                metadata.setContentEncoding("gzip");
            }
            metadata.setContentType(contentType);
            metadata.setContentLength(input.available());
            PutObjectRequest putObjectRequest = new PutObjectRequest(ossConfig.getBucketName(), filePath, input, metadata);
            client.putObject(putObjectRequest);
            return filePath;
        } catch (Exception e) {
            throw new OssException(e);
        }
    }

    @Override
    public InputStream getObject(String fileName) throws OssException {
        try {
            GetObjectRequest getObjectRequest = new GetObjectRequest(ossConfig.getBucketName(), fileName);
            return client.getObject(getObjectRequest).getObjectContent();
        } catch (Exception e) {
            throw new OssException(e);
        }
    }

    @Override
    public void remove(String path) throws OssException {
        try {
            client.deleteObject(ossConfig.getBucketName(), path);
        } catch (Exception e) {
            throw new OssException(e);
        }
    }

    @Override
    public void copyResource(String copy, String target) throws OssException {
        try {
            CopyObjectRequest request = new CopyObjectRequest()
                    .withSourceBucketName(ossConfig.getBucketName())
                    .withSourceKey(copy) // 源对象键（文件名）
                    .withDestinationBucketName(ossConfig.getBucketName())
                    .withDestinationKey(target); // 目标对象
            client.copyObject(request);
        } catch (Exception e) {
            throw new OssException(e);
        }
    }

    @Override
    public String generatePresignedUrl(String path, String contentType) throws OssException {
        long expiration = System.currentTimeMillis() + 10 * TimeUtil.MILLIS_PER_MINUTE;
        try {
            GeneratePresignedUrlRequest request =
                    new GeneratePresignedUrlRequest(ossConfig.getBucketName(), path)
                            .withMethod(HttpMethod.PUT)
//                            .withContentType(contentType)
                            .withExpiration(new Date(expiration));
            return client.generatePresignedUrl(request).toString();
        } catch (Exception e) {
            throw new OssException(e);
        }
    }
}
