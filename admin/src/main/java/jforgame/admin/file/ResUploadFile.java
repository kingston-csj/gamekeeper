package jforgame.admin.file;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResUploadFile {

    private String id;

    private String url;

    private long size;

    private long time;

    private String name;

}