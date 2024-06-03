package jforgame.admin.gamecmd.cmd;

import jforgame.admin.gamecmd.cmd.http.BanPlayerChatCmd;
import jforgame.admin.gamecmd.cmd.http.BanPlayerLoginCmd;
import jforgame.admin.gamecmd.cmd.http.HotSwapCmd;
import jforgame.admin.gamecmd.cmd.http.QueryPlayerCmd;
import jforgame.admin.gamecmd.cmd.http.ReloadConfigCmd;
import jforgame.admin.gamecmd.cmd.http.RunScriptCmd;

import java.util.HashMap;
import java.util.Map;

public enum CmdTypes {


    HOT_SWAP(1, "热更", "无", CmdTypes.TYPE_SERVER, HotSwapCmd.class),
    RUN_SCRIPT(2, "执行脚本", "Groovy脚本内容", CmdTypes.TYPE_SERVER, RunScriptCmd.class),
    RELOAD_CONFIG(3, "表格重载", "表格名称", CmdTypes.TYPE_SERVER, ReloadConfigCmd.class),
//    CLOSE_SERVER(4, "关服", "无", CmdTypes.TYPE_SERVER, CloseServerCmd.class),


    QUERY_PLAYER(100, "查询玩家信息", "角色uid或者模糊昵称", CmdTypes.TYPE_PLAYER, QueryPlayerCmd.class),
    BAN_LOGIN(101, "封号", "角色uid和封号时间", CmdTypes.TYPE_PLAYER, BanPlayerLoginCmd.class),
    BAN_CHAT(102, "禁言", "角色uid和禁言时间", CmdTypes.TYPE_PLAYER, BanPlayerChatCmd.class),

    ;

    final int id;

    final String name;

    final String params;

    final int type;

    final Class clazz;

    public static final int TYPE_SERVER = 1;
    public static final int TYPE_PLAYER = 2;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getParams() {
        return params;
    }

    public int getType() {
        return type;
    }

    public Class getClazz() {
        return clazz;
    }

    CmdTypes(int id, String name, String params, int type, Class clazz) {
        this.id = id;
        this.name = name;
        this.params = params;
        this.type = type;
        this.clazz = clazz;
    }

    private static final Map<Integer, CmdTypes> cmds = new HashMap<>();

    static {
        for (CmdTypes cmd : CmdTypes.values()) {
            cmds.put(cmd.getId(), cmd);
        }
    }

    public static CmdTypes queryCmd(int type) {
        return cmds.get(type);
    }

}
