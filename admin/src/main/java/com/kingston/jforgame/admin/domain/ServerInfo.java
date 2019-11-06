package com.kingston.jforgame.admin.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="t_server")
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

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }

    public Integer getPort() {
        return port;
    }

    public int getHttpPort() {
        return httpPort;
    }

    public int getMerged() {
        return merged;
    }

    @Override
    public String toString() {
        return "ServerInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }
}
