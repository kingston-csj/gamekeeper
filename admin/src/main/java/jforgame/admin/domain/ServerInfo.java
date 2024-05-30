package jforgame.admin.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="t_server")
@Data
public class ServerInfo {

    @Id
    private Integer id;
    @Column(name="name")
    private String name;
    @Column
    private String ip;

    @Column
    private int httpPort;

    @Column(name="jmxPort")
    private Integer port;

    /**
     * 大于0表示合区目标服
     */
    @Column
    private int merged;

}
