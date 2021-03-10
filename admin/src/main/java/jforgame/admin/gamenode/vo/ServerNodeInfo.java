package jforgame.admin.gamenode.vo;

/**
 * @Author: Kinson
 * @Date: 2019/10/26 18:18
 */
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
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
}
