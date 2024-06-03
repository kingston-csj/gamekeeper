package jforgame.admin.file;

import jforgame.admin.oss.FileTypes;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name="t_oss")
public class OssRecord {

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
