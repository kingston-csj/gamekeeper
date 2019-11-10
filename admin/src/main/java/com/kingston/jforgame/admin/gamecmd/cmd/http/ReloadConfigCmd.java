package com.kingston.jforgame.admin.gamecmd.cmd.http;

import com.kingston.jforgame.admin.domain.ServerInfo;
import com.kingston.jforgame.admin.gamecmd.cmd.CmdTypes;

import java.util.HashMap;
import java.util.Map;

public class ReloadConfigCmd extends HttpServerAdminCmd {

    private String tableName;

    public ReloadConfigCmd(ServerInfo serverNode, String tableName) {
        super(serverNode, tableName);
        this.tableName = tableName;
    }

    @Override
    public String httpMethod() {
        return "reloadConfig";
    }

    @Override
    public String action() {
        Map<String, String> params = new HashMap<>();
        params.put("tableName", tableName);
        return httpPost(url(), params);
    }

    @Override
    public CmdTypes meta() {
        return CmdTypes.RELOAD_CONFIG;
    }
}
