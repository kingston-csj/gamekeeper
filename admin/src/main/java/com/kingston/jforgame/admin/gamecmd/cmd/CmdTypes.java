package com.kingston.jforgame.admin.gamecmd.cmd;

import java.util.HashMap;
import java.util.Map;

public enum CmdTypes {


    HOT_SWAP(1, "热更", "无"),
    QUERY_PLAYER(2, "查询玩家信息", "角色uid或者模糊昵称"),
    BAN_LOGIN(3, "封号", "角色uid和封号时间"),
    BAN_CHAT(4, "禁言", "角色uid和禁言时间"),

    ;

    int type;

    String name;

    String params;

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getParams() {
        return params;
    }

    CmdTypes(int type, String name, String params) {
        this.type = type;
        this.name = name;
        this.params = params;
    }

    private static Map<Integer, CmdTypes> cmds = new HashMap<>();

    static {
        for (CmdTypes cmd : CmdTypes.values()) {
            cmds.put(cmd.getType(), cmd);
        }
    }

}
