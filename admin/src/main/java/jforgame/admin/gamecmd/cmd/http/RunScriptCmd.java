package jforgame.admin.gamecmd.cmd.http;

import jforgame.admin.domain.ServerInfo;
import jforgame.admin.gamecmd.cmd.CmdTypes;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class RunScriptCmd extends HttpServerAdminCmd {

    private final String script;

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
        if (!StringUtils.hasLength(script)) {
            return "Exception:IllegalArgument";
        }
        params.put("command", script);
        return httpPost(url(), params);
    }

    @Override
    public CmdTypes meta() {
        return CmdTypes.RUN_SCRIPT;
    }
}
