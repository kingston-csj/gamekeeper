package jforgame.admin.s3;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileTypes {


    PICTURE("picture", "图片"),

    VIDEO("video", "视频"),

    SOUND("sound", "声音"),

    FILE("file", "文件"),

    RAR("rar", "压缩文件"),

    OTHER("other", "其他"),

    ;


    String path;

    String desc;
}
