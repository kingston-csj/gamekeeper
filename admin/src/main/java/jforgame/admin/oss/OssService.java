package jforgame.admin.oss;

import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class OssService {

    @Autowired
    MinioProperties minioProperties;

    private Map<String, String> allowFileTypes = new HashMap<>();

    private Set<String> alllowPictureTypes = new HashSet<>();

    @PostConstruct
    private void init() {
        allowFileTypes.put("jpg", FileTypes.PICTURE.getPath());
        allowFileTypes.put("png", FileTypes.PICTURE.getPath());
        allowFileTypes.put("jpeg", FileTypes.PICTURE.getPath());
        allowFileTypes.put("gif", FileTypes.PICTURE.getPath());
        allowFileTypes.put("mp3", FileTypes.SOUND.getPath());
        allowFileTypes.put("mp4", FileTypes.VIDEO.getPath());

        allowFileTypes.put("ttf", FileTypes.FONT.getPath());

        alllowPictureTypes.add("jpg");
        alllowPictureTypes.add("png");
        alllowPictureTypes.add("jpeg");
        alllowPictureTypes.add("gif");
    }

    public boolean isPicture(String suffix) {
        return alllowPictureTypes.contains(suffix);
    }


    /**
     * 图片全路径，包括ossUrl+图片本身路径
     *
     * @param fileName
     * @return
     */
    public String fullImagePath(String fileName) {
        String stuff = fileName.substring(fileName.lastIndexOf(".") + 1);
        String fullName = minioProperties.getSubCatalog() + getPathOf(stuff) + "/" + fileName;
        return fullOssPath(fullName);
    }

    /**
     * 图片在oss内部路径
     *
     * @param fileName
     * @return
     */
    public String ossImagePath(String fileName) {
        String stuff = fileName.substring(fileName.lastIndexOf(".") + 1);
        return minioProperties.getSubCatalog() + getPathOf(stuff) + "/" + fileName;
    }

    public String fullOssPath(String filePath) {
        String url = "";
        if (StringUtils.isEmpty(minioProperties.getCdn())) {
            url =  minioProperties.getEndpoint() + "/" + minioProperties.getBucketName() + "/" + filePath;
        } else {
            // cdn路径保护桶地址了
            url =  minioProperties.getCdn() + "/" + filePath;
        }
        return url.replace("https", "http");
    }

    public String getPathOf(String type) {
        return allowFileTypes.getOrDefault(type, FileTypes.OTHER.getPath());
    }


    public boolean allowFileType(String stuff) {
        return allowFileTypes.containsKey(stuff);
    }
}
