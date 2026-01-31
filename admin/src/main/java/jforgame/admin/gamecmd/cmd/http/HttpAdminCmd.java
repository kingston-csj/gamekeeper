package jforgame.admin.gamecmd.cmd.http;

import jforgame.admin.core.SpringContext;
import jforgame.admin.domain.ServerInfo;
import jforgame.admin.gamecmd.cmd.AdminCmd;
import jforgame.admin.http.HttpClientService;
import jforgame.admin.logger.LoggerUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Map;

public abstract class HttpAdminCmd implements AdminCmd {

    protected ServerInfo serverNode;

    public HttpAdminCmd(ServerInfo serverNode) {
        this.serverNode = serverNode;
    }

    /**
     * 游戏服http地址
     *
     * @return
     */
    public String url() {
        return String.format("http://%s/api/%s", getGameHost(serverNode), httpMethod());
    }

    public abstract String httpMethod();

    private String getGameHost(ServerInfo server) {
        return server.getIp() + ":" + server.getHttpPort();
    }

    protected AdminHttpResponse httpPost(String url, Map<String, Object> params) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpClientService restTemplate = SpringContext.getBean(HttpClientService.class);
        try {
            AdminHttpResponse response = restTemplate.post(url, params, AdminHttpResponse.class);
            return response;
        } catch (Exception e) {
            LoggerUtil.error("", e);
            return AdminHttpResponse.failed(e.getMessage());
        }
    }
}
