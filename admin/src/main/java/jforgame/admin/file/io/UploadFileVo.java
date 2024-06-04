package jforgame.admin.file.io;

import lombok.Builder;
import lombok.Data;

import java.io.InputStream;

@Data
@Builder
public class UploadFileVo {

    private InputStream inputStream;

    //oss存放目录
    private String catalog;

    private String fileName;

    private long size;

    private String contentType;
}
