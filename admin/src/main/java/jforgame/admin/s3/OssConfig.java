package jforgame.admin.s3;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "s3")
@Component
@Data
public class OssConfig {

    // amazon统一为s3.amazonaws.com，MinIO可自定义
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
    private String subCatalog;
    // cdn域名，只在读取文件使用，上传文件统一用endpoint
    private String cdn;
    // 使用minio，该值可以配置为amazon任何一个有效区域，唯一作用只是为了兼容amazonAPI
    private String region;
    // 清除cdn緩存的api地址(只在预发布服与正式服执行)
    private String purgeApi;
    // 清除cdn緩存的token
    private String purgeToken;


    public String getSubCatalog() {
        if (subCatalog == null || "".equalsIgnoreCase(subCatalog.trim())) {
            return "";
        }
        return subCatalog;
    }
}