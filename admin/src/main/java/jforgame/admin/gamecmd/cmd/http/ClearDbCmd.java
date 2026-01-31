package jforgame.admin.gamecmd.cmd.http;

import jforgame.admin.domain.ServerInfo;
import jforgame.admin.gamecmd.cmd.CmdTypes;

import java.util.HashMap;
import java.util.Map;

public class ClearDbCmd extends HttpServerAdminCmd {

    public ClearDbCmd(ServerInfo serverNode, String tableName) {
        super(serverNode, tableName);
    }

    @Override
    public String httpMethod() {
        return "clearDb";
    }

    @Override
    public AdminHttpResponse action() {
        Map<String, Object> params = new HashMap<>();
        return httpPost(url(), params);
    }

    @Override
    public CmdTypes meta() {
        return CmdTypes.Clear_DB;
    }
}
