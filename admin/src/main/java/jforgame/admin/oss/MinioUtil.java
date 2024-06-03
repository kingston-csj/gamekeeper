package jforgame.admin.oss;

import jforgame.admin.file.UploadFileVo;
import jforgame.commons.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class MinioUtil {

    @Autowired
    private MinioProperties minioProperties;
    @Autowired
    private MinIoClientProxy minioClient;

    @Autowired
    OssService ossService;

    public Pair<String, String> uploadFile(UploadFileVo file) throws Exception {
        if (file == null || file.getSize() == 0) {
            log.error("==> 上传文件异常：文件大小为空 ...");
            throw new RuntimeException("illegal file size");
        }
        String originalFileName = file.getFileName();
        String key = UUID.randomUUID().toString().replace("-", "");;
        String suffix = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);

        String objectName = String.format("%s%s", key, "." + suffix);
        String fullPath = minioProperties.getSubCatalog() + file.getCatalog() + "/" + objectName;

        minioClient.upload(file.getInputStream(), fullPath, file.getContentType());

        String url = ossService.fullOssPath(fullPath);
        return new Pair<>(objectName, url);
    }

}