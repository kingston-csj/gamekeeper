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

    private long money;

    /**
     * 禁言结束时间
     */
    private long banChat;
    /**
     * 封号结束时间
     */
    private long banLogin;

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

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public long getBanChat() {
        return banChat;
    }

    public void setBanChat(long banChat) {
        this.banChat = banChat;
    }

    public long getBanLogin() {
        return banLogin;
    }

    public void setBanLogin(long banLogin) {
        this.banLogin = banLogin;
    }
}
