package jforgame.admin.gamecmd.cmd.http;

import jforgame.admin.domain.ServerInfo;
import jforgame.admin.gamecmd.cmd.CmdTypes;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class ReloadDirtyWordsScriptCmd extends HttpServerAdminCmd {


    public ReloadDirtyWordsScriptCmd(ServerInfo serverNode, String script) {
        super(serverNode, script);
    }

    @Override
    public String httpMethod() {
        return "reloadDirtyWords";
    }

    @Override
    public AdminHttpResponse action() {
        Map<String, Object> params = new HashMap<>();
        return httpPost(url(), params);
    }

    @Override
    public CmdTypes meta() {
        return CmdTypes.RELOAD_DIRTY_WORDS;
    }
}
