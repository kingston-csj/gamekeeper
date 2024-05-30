package jforgame.admin.gamecmd.cmd.http;

import jforgame.admin.domain.ServerInfo;
import jforgame.admin.gamecmd.cmd.CmdTypes;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class HotSwapCmd extends HttpServerAdminCmd {

    private final String path;

    public HotSwapCmd(ServerInfo serverNode, String params) {
        super(serverNode, params);
        this.path = params;
    }

    @Override
    public String httpMethod() {
        return "hotswap";
    }

    @Override
    public String action() {
        Map<String, String> params = new HashMap<>();
        if (!StringUtils.hasLength(path)) {
            return "Exception:IllegalArgument";
        }
        params.put("command", path);
        return httpPost(url(), params);
    }

    @Override
    public CmdTypes meta() {
        return CmdTypes.HOT_SWAP;
    }
}
