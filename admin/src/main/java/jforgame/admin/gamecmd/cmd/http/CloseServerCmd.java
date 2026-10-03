package jforgame.admin.gamecmd.cmd.http;

import jforgame.admin.domain.ServerInfo;
import jforgame.admin.gamecmd.cmd.CmdTypes;

import java.util.HashMap;
import java.util.Map;

public class CloseServerCmd extends HttpServerAdminCmd {

    public CloseServerCmd(ServerInfo serverNode, String params) {
        super(serverNode, params);
    }

    @Override
    public String httpMethod() {
        return "closeServer";
    }

    @Override
    public AdminHttpResponse action() {
        Map<String, Object> params = new HashMap<>();
        return httpPost(url(), params);
    }

    @Override
    public CmdTypes meta() {
        return CmdTypes.CLOSE_SERVER;
    }
}
