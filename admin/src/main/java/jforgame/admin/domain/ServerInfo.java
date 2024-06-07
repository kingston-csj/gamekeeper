package jforgame.admin.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="t_server")
@Data
@NoArgsConstructor
public class ServerInfo {

    @Id
    private Integer id;
    @Column(name="name")
    private String name;
    @Column
    private String ip;
    @Column
    private Integer port;

    @Column
    private int httpPort;
    /**
     * 大于0表示合区目标服
     */
    @Column
    private int merged;

}
