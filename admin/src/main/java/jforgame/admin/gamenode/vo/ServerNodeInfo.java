package jforgame.admin.gamenode.vo;

import lombok.Data;

@Data
public class ServerNodeInfo {

    private Integer id;
    private String name;
    private String ip;

    private Integer port;
    /**
     * 在线人数
     */
    private int onlinePlayerSum;
    /**
     * 缓存人数
     */
    private int cachePlayerSum;

}
