package jforgame.admin.s3;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.minio.MinioClient;
import okhttp3.OkHttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Configuration
public class OssAutoConfig {


    @ConditionalOnProperty(name = "s3.type", havingValue = "minio")
    public static class MinioAutoConfig {

        @Autowired
        private OssConfig ossConfig;

        @Bean
        public S3Client createS3Client() throws Exception {
            MinioClient minioClient = minioClient();
            return new MinIoClientProxy(minioClient, ossConfig);
        }

        private MinioClient minioClient() throws Exception {
            OkHttpClient okHttpClient = getUnsafeOkHttpClient();
            return MinioClient.builder().endpoint(ossConfig.getEndpoint())
                    .region(ossConfig.getRegion())
                    .credentials(ossConfig.getAccessKey(), ossConfig.getSecretKey())
                    .httpClient(okHttpClient)
                    .build();
        }

        private static OkHttpClient getUnsafeOkHttpClient() throws Exception {
            // 不验证ssl证书，理由是mongo已经使用了证书了，不兼容
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }
            }};
            // 创建一个SSL上下文，使用上面的TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            // 创建OkHttpClient时使用新的方法
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true); // 允许所有主机名
            return builder.build();
        }
    }


    @ConditionalOnProperty(name = "s3.type", havingValue = "amazon")
    public static class AmazonAutoConfig {
        @Autowired
        private OssConfig ossConfig;

        @Bean
        public S3Client createS3Client() throws Exception {
            SSLContext sslContext = createTrustAllSSLContext();
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                    sslContext,
                    new String[]{"TLSv1.2"},
                    null,
                    NoopHostnameVerifier.INSTANCE);

            // 创建ClientConfiguration
            ClientConfiguration clientConfig = new ClientConfiguration();
            clientConfig.getApacheHttpClientConfig().setSslSocketFactory(sslConnectionSocketFactory);

            AWSCredentials credentials = new BasicAWSCredentials(ossConfig.getAccessKey(), ossConfig.getSecretKey());
            AwsClientBuilder.EndpointConfiguration endpointConfig = new AwsClientBuilder.EndpointConfiguration(
                    ossConfig.getEndpoint(), // amazon统一为s3.amazonaws.com，MinIO可自定义
                    ossConfig.getRegion()// 签名区域，对于 MinIO 来说，这个值可以是任意字符串
            );
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration(endpointConfig)
                    // 设置自定义的SSLSocketFactory
                    .withClientConfiguration(clientConfig)
                    .build();

            return new AmazonS3Client(s3Client, ossConfig);
        }
    }

    private static SSLContext createTrustAllSSLContext() throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        return sc;
    }

}
