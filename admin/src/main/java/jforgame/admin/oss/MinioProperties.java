package jforgame.admin.oss;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "minio")
@Component
@Data
public class MinioProperties {
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
    private String subCatalog;
    // cdn域名，只在读取文件使用，上传文件统一用endpoint
    private String cdn;

    public String getSubCatalog() {
        if (subCatalog == null) {
            return "";
        }
        return subCatalog;
    }
}