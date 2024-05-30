package jforgame.admin.monitor.vo;

import lombok.Data;

/**
 * 游戏服健康监控指标
 */
@Data
public class ServerMonitorNode {

    /**
     * 服务器id
     */
    private int serverId;
    /**
     * 在线人数
     */
    private int onlinePlayerSum;
    /**
     * 缓存人数
     */
    private int cachePlayerSum;

    /**
     * 持久化队列玩家数量
     */
    private int playerDbQueueSum;

    /**
     * 收到的时间戳
     */
    private long heartTime;

}
