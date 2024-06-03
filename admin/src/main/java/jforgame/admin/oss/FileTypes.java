package jforgame.admin.oss;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileTypes {


    PICTURE("picture", "图片"),

    VIDEO("video", "视频"),

    SOUND("sound", "声音"),

    FILE("file", "文件"),

    PACK("pack", "游戏打包数据"),

    FONT("font", "编辑器客户端字体"),

    OTHER("other", "其他"),

    ;


    String path;

    String desc;
}
