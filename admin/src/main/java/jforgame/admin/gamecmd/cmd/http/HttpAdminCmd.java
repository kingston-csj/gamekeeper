package jforgame.admin.gamecmd.cmd.http;

import jforgame.admin.core.SpringContext;
import jforgame.admin.domain.ServerInfo;
import jforgame.admin.gamecmd.cmd.AdminCmd;
import jforgame.admin.http.HttpClientService;
import jforgame.admin.logger.LoggerUtil;
import jforgame.commons.JsonUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Map;

@Slf4j
public abstract class HttpAdminCmd implements AdminCmd {

    protected ServerInfo serverNode;

    @Getter
    protected String params;

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
            log.info("HttpAdminCmd execute, url {}, response {}", url, JsonUtil.object2String(response));
            return response;
        } catch (Exception e) {
            LoggerUtil.error("", e);
            return AdminHttpResponse.failed(e.getMessage());
        }
    }

    protected AdminHttpResponse httpGet(String url, Map<String, Object> params) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpClientService restTemplate = SpringContext.getBean(HttpClientService.class);
        try {
            String response = restTemplate.get(url, params);
            log.info("HttpAdminCmd execute, url {}, response {}", url, JsonUtil.object2String(response));
            return JsonUtil.string2Object(response, AdminHttpResponse.class);
        } catch (Exception e) {
            LoggerUtil.error("", e);
            return AdminHttpResponse.failed(e.getMessage());
        }
    }
}
