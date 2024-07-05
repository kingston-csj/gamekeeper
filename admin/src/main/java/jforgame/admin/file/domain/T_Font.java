package jforgame.admin.file.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;

import jakarta.persistence.Id;

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
