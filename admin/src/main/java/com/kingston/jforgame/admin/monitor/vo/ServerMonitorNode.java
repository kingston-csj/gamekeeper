package com.kingston.jforgame.admin.monitor.vo;

/**
 * 游戏服健康监控指标
 */
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

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getOnlinePlayerSum() {
        return onlinePlayerSum;
    }

    public void setOnlinePlayerSum(int onlinePlayerSum) {
        this.onlinePlayerSum = onlinePlayerSum;
    }

    public int getCachePlayerSum() {
        return cachePlayerSum;
    }

    public void setCachePlayerSum(int cachePlayerSum) {
        this.cachePlayerSum = cachePlayerSum;
    }

    public int getPlayerDbQueueSum() {
        return playerDbQueueSum;
    }

    public void setPlayerDbQueueSum(int playerDbQueueSum) {
        this.playerDbQueueSum = playerDbQueueSum;
    }

    public long getHeartTime() {
        return heartTime;
    }

    public void setHeartTime(long heartTime) {
        this.heartTime = heartTime;
    }
}
