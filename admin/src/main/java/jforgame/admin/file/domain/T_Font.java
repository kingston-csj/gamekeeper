package jforgame.admin.file.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "t_font")
public class T_Font {

    @Id
    private String id;

    /**
     * oss地址
     */
    @Column
    private String url;

    @Column
    private String name;

    @Column
    private String description;
}
