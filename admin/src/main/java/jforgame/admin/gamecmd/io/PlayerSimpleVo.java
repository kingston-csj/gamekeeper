package jforgame.admin.gamecmd.io;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerSimpleVo {

    private int serverId;

    private String account;

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

}
