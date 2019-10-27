package com.kingston.jforgame.admin.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="t_server")
public class ServerInfo {

    @Id
    private Integer id;
    @Column(name="name")
    private String title;
    @Column
    private String ip;
    @Column(name="jmxPort")
    private Integer port;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "ServerInfo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }
}
