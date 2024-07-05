package jforgame.admin.file.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jforgame.admin.oss.FileTypes;
import lombok.Data;
import jakarta.persistence.Id;

@Data
@Entity(name="t_picture")
public class T_Picture {

    @Id
    private String id;

    /**
     * oss地址
     */
     @Column
    private String url;
    /**
     * 类型 {@link FileTypes}
     */
     @Column
    private byte type;

     @Column
    private String name;

     @Column
    private long createTime;

     @Column
    private long size;

     @Column
    private int width;

     @Column
    private int height;
}
