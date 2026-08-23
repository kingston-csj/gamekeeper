package jforgame.admin.gamenode.io;

import lombok.Data;

@Data
public class ServerNodeInfo {

    private int id;
    private String name;
    private String ip;

    private int port;

    /**
     * http端口
     */
    private int httpPort;


    /**
     * 是否使用网关,0:否,1:是
     */
    private int useGate;

}
