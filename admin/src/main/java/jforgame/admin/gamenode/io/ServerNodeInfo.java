package jforgame.admin.gamenode.io;

import lombok.Data;

@Data
public class ServerNodeInfo {

    private int id;
    private String name;
    private String ip;

    /**
     * 所属机器节点id
     */
    private int machineId;

    private int port;

    /**
     * http端口
     */
    private int httpPort;

    /**
     * 在线玩家数
     */
    private int onlinePlayerSum;


    /**
     * 是否使用网关,0:否,1:是
     */
    private int useGate;

    /**
     * 服务器类型, java/go
     */
    private String serverType;



    private String protocolAPI;
}
