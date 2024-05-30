package jforgame.admin.gamecmd.cmd.http;

import jforgame.admin.core.SpringContext;
import jforgame.admin.domain.ServerInfo;
import jforgame.admin.gamecmd.cmd.AdminCmd;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public abstract class HttpAdminCmd implements AdminCmd {

    protected ServerInfo serverNode;

    public HttpAdminCmd(ServerInfo serverNode) {
        this.serverNode = serverNode;
    }

    /**
     * 游戏服http地址
     * @return
     */
    public String url() {
        return String.format("http://%s/admin/%s", getGameHost(serverNode), httpMethod());
    }

    public abstract String httpMethod();

    private String getGameHost(ServerInfo server) {
        return server.getIp() + ":" + server.getHttpPort();
    }

    protected String httpGet(String url, Object... uriVariables) {
        RestTemplate restTemplate = SpringContext.getBean(RestTemplate.class);
        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class, uriVariables);
        return result.getBody();
    }

    protected String httpPost(String url, Map<String, String> params) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity requestEntity = new HttpEntity(params, headers);
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = SpringContext.getBean(RestTemplate.class);
        return restTemplate.postForObject(url, requestEntity, String.class);
    }
}
