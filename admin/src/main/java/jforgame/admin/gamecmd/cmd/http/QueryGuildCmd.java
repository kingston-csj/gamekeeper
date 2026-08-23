package jforgame.admin.gamecmd.cmd.http;

import jforgame.admin.domain.ServerInfo;
import jforgame.admin.gamecmd.cmd.CmdTypes;
import jforgame.admin.http.PageResult;
import jforgame.commons.JsonUtil;

import java.util.HashMap;
import java.util.Map;

public class QueryGuildCmd extends HttpAdminCmd {

    private String sign;
    private int pageNum;
    private int pageSize;

    public QueryGuildCmd(ServerInfo serverNode, String sign, int pageNum, int pageSize) {
        super(serverNode);
        this.sign = sign;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    @Override
    public String httpMethod() {
        return "queryGuilds";
    }

    @Override
    public PageResult action() {
        String url = url();
        Map<String, Object> params = new HashMap<>();
        params.put("pageNum", pageNum);
        params.put("pageSize", pageSize);
        params.put("sign", sign);
        AdminHttpResponse response = httpGet(url, params);
        return JsonUtil.string2Object(response.getData(), PageResult.class);
    }

    @Override
    public CmdTypes meta() {
        return CmdTypes.QUERY_GUILD;
    }
}
