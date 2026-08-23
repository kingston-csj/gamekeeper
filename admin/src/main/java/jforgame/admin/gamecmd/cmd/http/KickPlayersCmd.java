package jforgame.admin.gamecmd.cmd.http;

import jforgame.admin.domain.ServerInfo;
import jforgame.admin.gamecmd.cmd.CmdTypes;

import java.util.HashMap;
import java.util.Map;

public class KickPlayersCmd extends HttpServerAdminCmd {



    public KickPlayersCmd(ServerInfo serverNode, String playerIds) {
        super(serverNode, playerIds);
    }

    @Override
    public String httpMethod() {
        return "kickPlayers";
    }

    @Override
    public AdminHttpResponse action() {
        Map<String, Object> params = new HashMap<>();
        params.put("params", getParams());
        return httpPost(url(), params);
    }

    @Override
    public CmdTypes meta() {
        return CmdTypes.Kick_Player;
    }
}
