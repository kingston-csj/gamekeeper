package jforgame.admin.gamecmd.cmd.http;

import jforgame.admin.domain.ServerInfo;
import jforgame.admin.gamecmd.cmd.CmdTypes;
import jforgame.commons.JsonUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryLogTypesCmd extends HttpAdminCmd {


    public QueryLogTypesCmd(ServerInfo serverNode) {
        super(serverNode);
    }

    @Override
    public String httpMethod() {
        return "queryLogTypes";
    }

    @Override
    public List<String> action() {
        String url = url();
        Map<String, Object> params = new HashMap<>();
        AdminHttpResponse response = httpGet(url, params);
        if (response == null || response.getCode() != AdminHttpResponse.SUCC) {
            return Collections.emptyList();
        }
        return JsonUtil.string2Object(response.getData(), List.class);
    }

    @Override
    public CmdTypes meta() {
        return CmdTypes.QUERY_LOG_TYPES;
    }
}
