package jforgame.admin.s3;

import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class OssService {

    @Autowired
    OssConfig ossConfig;

    private Map<String, String> allowFileTypes = new HashMap<>();

    private Map<String, String> contentTypes = new HashMap<>();

    private Set<String> alllowPictureTypes = new HashSet<>();

    @Autowired
    Environment environment;

    // 外网环境，需要https
    private boolean needHttps;

    Set<String> otherFileTypes = new HashSet<>();

    @PostConstruct
    private void init() {
        String activatedProfile = environment.getProperty("spring.profiles.active");
        needHttps = "test".equalsIgnoreCase(activatedProfile) || "prev".equalsIgnoreCase(activatedProfile) || "prod".equalsIgnoreCase(activatedProfile);

        allowFileTypes.put("jpg", FileTypes.PICTURE.getPath());
        allowFileTypes.put("jpeg", FileTypes.PICTURE.getPath());
        allowFileTypes.put("png", FileTypes.PICTURE.getPath());
        allowFileTypes.put("gif", FileTypes.PICTURE.getPath());
        allowFileTypes.put("mp3", FileTypes.SOUND.getPath());
        allowFileTypes.put("mp4", FileTypes.VIDEO.getPath());

        otherFileTypes.add("json");
        otherFileTypes.add("txt");
        otherFileTypes.add("atlas");

        alllowPictureTypes.add("jpg");
        alllowPictureTypes.add("png");
        alllowPictureTypes.add("jpeg");
        alllowPictureTypes.add("gif");

        contentTypes.put("jpg", "image/jpeg");
        contentTypes.put("jpeg", "image/jpeg");
        contentTypes.put("png", "image/png");
        contentTypes.put("gif", "image/gif");
        contentTypes.put("mp3", "audio/mpeg");
        contentTypes.put("mp4", "video/mp4");
        contentTypes.put("ttf", "font/ttf");
        contentTypes.put("json", "application/json");
        contentTypes.put("txt", "text/plain");
        contentTypes.put("atlas", "text/plain");
    }


    /**
     * 完整cdn路径，分成三个部分：
     * s3本身http地址（桶地址）+程序内部二级目录+文件名
     *
     * @param filePath
     * @return
     */
    public String fullCdnPath(String filePath) {
        String url = "";
        if (StringUtils.isEmpty(ossConfig.getCdn())) {
            url = ossConfig.getEndpoint() + "/" + ossConfig.getBucketName() + "/" + filePath;
        } else {
            // cdn路径包括桶地址了
            url = ossConfig.getCdn() + "/" + filePath;
        }
        if (needHttps) {
            return url;
        }
        return url.replace("https", "http");
    }

    /**
     * 完整oss路径，分成三个部分：
     * <br>不</br>使用cdn，用于同一个链接内容实时变化的资源
     */
    public String fullRawOssPath(String filePath) {
        String url = ossConfig.getEndpoint() + "/" + ossConfig.getBucketName() + "/" + filePath;
        if (needHttps) {
            return url;
        }
        return url.replace("https", "http");
    }


    public String getPathOf(String type) {
        type = type.toLowerCase();
        return allowFileTypes.getOrDefault(type, FileTypes.OTHER.getPath());
    }

    /**
     * 除了白名单里的类型，其他都是不允许的
     *
     * @param type 文件后缀
     * @return true表示禁止 false表示允许
     */
    public boolean forbidFileType(String type) {
        type = type.toLowerCase();
        return !allowFileTypes.containsKey(type) && !otherFileTypes.contains(type);
    }

    public String getContentType(String type) {
        return contentTypes.getOrDefault(type, "application/octet-stream");
    }
}
