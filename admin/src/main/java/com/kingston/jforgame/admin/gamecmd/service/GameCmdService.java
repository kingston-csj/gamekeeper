package com.kingston.jforgame.admin.gamecmd.service;

import com.kingston.jforgame.admin.domain.ServerInfo;
import com.kingston.jforgame.admin.gamecmd.vo.PlayerSimpleVo;
import com.kingston.jforgame.admin.gamecmd.vo.SimplePlayerQueryResult;
import com.kingston.jforgame.admin.gamenode.service.ServerNodeService;
import com.kingston.jforgame.admin.security.SecurityUtils;
import com.kingston.jforgame.admin.utils.JsonUtil;
import com.kingston.jforgame.admin.utils.SimplyReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GameCmdService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ServerNodeService serversManager;

    public String hotSwap(List<Integer> servers) {
        if (!SecurityUtils.hasAuth("ADMIN")) {
            return "权限不够";
        }
        // TODO 改成异步多线程执行！！
        Map<Integer, String> result = new HashMap<>();
        for (Integer serverId : servers) {
            ServerInfo server = serversManager.getServerNodeBy(serverId);
            if (server != null) {
                try {
                    String url = String.format("http://%s/serverController/hotSwap", getGameHost(server));
                    String info = httpGet(url);
                    result.put(serverId, info);
                } catch (Exception e) {
                    result.put(serverId, e.getMessage());
                }
            }
        }
        return result.toString();
    }

    /**
     * @param serverId
     * @param sign     昵称或角色id
     * @return
     */
    public SimplePlayerQueryResult queryPlayerSimple(int serverId, String sign) {
        ServerInfo server = serversManager.getServerNodeBy(serverId);
        SimplePlayerQueryResult result = new SimplePlayerQueryResult();
        if (server != null) {
            String url = String.format("http://%s/serverController/simplePlayer?sign=%s", getGameHost(server), sign);
            String json = httpGet(url);
            SimplyReply simplyReply = JsonUtil.string2Object(json, SimplyReply.class);
            List<PlayerSimpleVo> vos = JsonUtil.string2Collection(simplyReply.getMsg(), ArrayList.class, PlayerSimpleVo.class);

            result.setVos(vos);
        }
        return result;
    }

    /**
     * 封号
     */
    public SimplyReply banLogin(int serverId, long uid,
                                long endTime) {
        if (!SecurityUtils.hasAuth("ADMIN")) {
            return SimplyReply.valueOfFail("权限不够");
        }
        ServerInfo server = serversManager.getServerNodeBy(serverId);
        String url = String.format("http://%s/serverController/banLogin", getGameHost(server));
        Map<String, String> params = new HashMap<>();
        params.put("uid", "" + uid);
        params.put("endTime", "" + endTime);
        String postResult = httpPost(url, params);
        return JsonUtil.string2Object(postResult, SimplyReply.class);
    }

    /**
     * 禁言
     */
    public SimplyReply banChat(int serverId, long uid,
                                long endTime) {
        if (!SecurityUtils.hasAuth("ADMIN")) {
            return SimplyReply.valueOfFail("权限不够");
        }
        ServerInfo server = serversManager.getServerNodeBy(serverId);
        String url = String.format("http://%s/serverController/banChat", getGameHost(server));
        Map<String, String> params = new HashMap<>();
        params.put("uid", "" + uid);
        params.put("endTime", "" + endTime);
        String postResult = httpPost(url, params);
        return JsonUtil.string2Object(postResult, SimplyReply.class);
    }


    private String httpGet(String url, Object... uriVariables) {
        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class, uriVariables);
        return result.getBody();
    }

    private String httpPost(String url, Map<String, String> params) {
        MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<>();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            requestMap.add(entry.getKey(), entry.getValue());

        }
        HttpEntity requestEntity = new HttpEntity(requestMap, null);
        return restTemplate.postForObject(url, requestEntity, String.class);
    }

    private String getGameHost(ServerInfo server) {
        return server.getIp() + ":" + server.getHttpPort();
    }
}
