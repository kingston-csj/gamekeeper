package jforgame.admin.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name="t_server")
@Data
@NoArgsConstructor
public class ServerInfo {

    @Id
    private Integer id;
    @Column(name="name")
    private String name;
    @Column
    private int machineId;

    @Column
    private int httpPort;
    /**
     * 大于0表示合区目标服
     */
    @Column
    private int merged;

    /**
     * 是否使用网关,0:否,1:是
     */
    @Column
    private int useGate;

    /**
     * 服务器类型, java/go
     */
    @Column
    private String serverType;


}
