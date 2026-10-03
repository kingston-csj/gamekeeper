package jforgame.admin.gamecmd.cmd.http;

import jforgame.admin.domain.ServerInfo;
import jforgame.admin.gamecmd.cmd.CmdTypes;
import jforgame.commons.JsonUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class QueryLogReasonsCmd extends HttpAdminCmd {


    public QueryLogReasonsCmd(ServerInfo serverNode) {
        super(serverNode);
    }

    @Override
    public String httpMethod() {
        return "queryLogReasons";
    }

    @Override
    public Map<Integer, String> action() {
        String url = url();
        Map<String, Object> params = new HashMap<>();
        AdminHttpResponse response = httpGet(url, params);
        if (response == null || response.getCode() != AdminHttpResponse.SUCC) {
            log.info("queryLogReasons url {} no response  ", url);
            return Collections.emptyMap();
        }
        var result = JsonUtil.string2Map(response.getData(), Integer.class, String.class);
        log.info("queryLogReasons url {} response size {}", url, result.size());
        return result;
    }

    @Override
    public CmdTypes meta() {
        return CmdTypes.QUERY_LOG_REASONS;
    }
}
