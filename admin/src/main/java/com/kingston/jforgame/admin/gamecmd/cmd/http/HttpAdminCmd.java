package com.kingston.jforgame.admin.gamecmd.cmd.http;

import com.kingston.jforgame.admin.core.SpringContext;
import com.kingston.jforgame.admin.domain.ServerInfo;
import com.kingston.jforgame.admin.gamecmd.cmd.AdminCmd;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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
        return String.format("http://%s/serverController/%s", getGameHost(serverNode), httpMethod());
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
        MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<>();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            requestMap.add(entry.getKey(), entry.getValue());

        }
        HttpEntity requestEntity = new HttpEntity(requestMap, null);
        RestTemplate restTemplate = SpringContext.getBean(RestTemplate.class);
        return restTemplate.postForObject(url, requestEntity, String.class);
    }
}
