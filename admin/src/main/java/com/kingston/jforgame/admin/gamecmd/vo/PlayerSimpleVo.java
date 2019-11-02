package com.kingston.jforgame.admin.gamecmd.vo;

/**
 * @Author: Kingston
 * @Date: 2019/11/2 14:32
 */
public class PlayerSimpleVo {

    private int serverId;

    private String name;

    private long uid;

    private int level;

    private long gold;


    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getGold() {
        return gold;
    }

    public void setGold(long gold) {
        this.gold = gold;
    }
}
