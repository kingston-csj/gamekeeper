package jforgame.admin.gamecmd.cmd.http;

import jforgame.admin.domain.ServerInfo;
import jforgame.admin.gamecmd.cmd.CmdTypes;

import java.util.HashMap;
import java.util.Map;

public class BanPlayerChatCmd extends HttpAdminCmd {

    private String uid;

    private String endTime;

    public BanPlayerChatCmd(ServerInfo serverNode, String uid, String endTime) {
        super(serverNode);
        this.uid = uid;
        this.endTime = endTime;
    }

    @Override
    public String httpMethod() {
        return "banChat";
    }

    @Override
    public String action() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", "" + uid);
        params.put("endTime", "" + endTime);
        return httpPost(url(), params);
    }

    @Override
    public CmdTypes meta() {
        return CmdTypes.BAN_CHAT;
    }
}
