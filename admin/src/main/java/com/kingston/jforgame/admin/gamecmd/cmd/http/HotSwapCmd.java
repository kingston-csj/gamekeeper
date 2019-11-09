package com.kingston.jforgame.admin.gamecmd.cmd.http;

import com.kingston.jforgame.admin.domain.ServerInfo;
import com.kingston.jforgame.admin.gamecmd.cmd.CmdTypes;

public class HotSwapCmd extends HttpAdminCmd {

    public HotSwapCmd(ServerInfo serverNode) {
        super(serverNode);
    }

    @Override
    public String httpMethod() {
        return "hotSwap";
    }

    @Override
    public String action() {
        String url = url();
        return httpGet(url);
    }

    @Override
    public CmdTypes meta() {
        return CmdTypes.HOT_SWAP;
    }
}
