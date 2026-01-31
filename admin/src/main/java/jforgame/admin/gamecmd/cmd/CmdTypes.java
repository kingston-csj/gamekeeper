package jforgame.admin.gamecmd.cmd;

import jforgame.admin.gamecmd.cmd.http.BanPlayerChatCmd;
import jforgame.admin.gamecmd.cmd.http.BanPlayerLoginCmd;
import jforgame.admin.gamecmd.cmd.http.ClearDbCmd;
import jforgame.admin.gamecmd.cmd.http.HotSwapCmd;
import jforgame.admin.gamecmd.cmd.http.QueryPlayerCmd;
import jforgame.admin.gamecmd.cmd.http.ReloadConfigCmd;
import jforgame.admin.gamecmd.cmd.http.RunScriptCmd;

import java.util.HashMap;
import java.util.Map;

public enum CmdTypes {


    HOT_SWAP(1, "代码热更", "无", CmdTypes.TYPE_GAME, HotSwapCmd.class),
    RELOAD_CONFIG(2, "表格重载", "表格名称", CmdTypes.TYPE_GAME, ReloadConfigCmd.class),


    RUN_SCRIPT(101, "执行脚本", "Groovy脚本内容", CmdTypes.TYPE_SERVER, RunScriptCmd.class),
    Clear_DB(102, "清库", "无", CmdTypes.TYPE_SERVER, ClearDbCmd.class),


    QUERY_PLAYER(201, "查询玩家信息", "角色uid或者模糊昵称", CmdTypes.TYPE_PLAYER, QueryPlayerCmd.class),
    BAN_LOGIN(202, "封号", "角色uid和封号时间", CmdTypes.TYPE_PLAYER, BanPlayerLoginCmd.class),
    BAN_CHAT(203, "禁言", "角色uid和禁言时间", CmdTypes.TYPE_PLAYER, BanPlayerChatCmd.class),

    ;

    final int id;

    final String name;

    final String params;

    final int type;

    final Class clazz;

    /**
     * 命令类型：游戏运营命令
     */
    public static final int TYPE_GAME = 0;
    /**
     * 命令类型：游戏运维命令
     */
    public static final int TYPE_SERVER = 1;
    /**
     * 命令类型：玩家查询
     */
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
