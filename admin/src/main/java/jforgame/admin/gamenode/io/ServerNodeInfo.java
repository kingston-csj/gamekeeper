package jforgame.admin.gamenode.io;

import lombok.Data;

@Data
public class ServerNodeInfo {

    private Integer id;
    private String name;
    private String ip;

    private Integer port;

    /**
     * http端口
     */
    private int httpPort;

    /**
     * 在线人数
     */
    private int onlinePlayerSum;
    /**
     * 缓存人数
     */
    private int cachePlayerSum;

}
