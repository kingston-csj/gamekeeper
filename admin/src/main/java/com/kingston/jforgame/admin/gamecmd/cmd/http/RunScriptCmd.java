package com.kingston.jforgame.admin.gamecmd.cmd.http;

import com.kingston.jforgame.admin.domain.ServerInfo;
import com.kingston.jforgame.admin.gamecmd.cmd.CmdTypes;

import java.util.HashMap;
import java.util.Map;

public class RunScriptCmd extends HttpServerAdminCmd {

    private String script;

    public RunScriptCmd(ServerInfo serverNode, String script) {
        super(serverNode, script);
        this.script = script;
    }

    @Override
    public String httpMethod() {
        return "runScript";
    }

    @Override
    public String action() {
        Map<String, String> params = new HashMap<>();
        params.put("script", script);
        return httpPost(url(), params);
    }

    @Override
    public CmdTypes meta() {
        return CmdTypes.RUN_SCRIPT;
    }
}
