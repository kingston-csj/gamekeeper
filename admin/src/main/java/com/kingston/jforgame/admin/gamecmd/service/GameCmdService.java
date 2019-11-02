package com.kingston.jforgame.admin.gamecmd.service;

import com.kingston.jforgame.admin.domain.ServerInfo;
import com.kingston.jforgame.admin.gamecmd.vo.PlayerSimpleVo;
import com.kingston.jforgame.admin.gamecmd.vo.SimplePlayerQueryResult;
import com.kingston.jforgame.admin.gamenode.service.ServerNodeService;
import com.kingston.jforgame.admin.security.SecurityUtils;
import com.kingston.jforgame.admin.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
                    String url = String.format("http://localhost:%s/serverController/hotSwap", server.getHttpPort());
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
     *
     * @param serverId
     * @param sign 昵称或角色id
     * @return
     */
    public SimplePlayerQueryResult queryPlayerSimple(int serverId, String sign) {
        ServerInfo server = serversManager.getServerNodeBy(serverId);
        String url = String.format("http://localhost:%s/serverController/simplePlayer", server.getHttpPort(), "sign", sign);
        String json = httpGet(url);
        List<PlayerSimpleVo> vos = JsonUtil.string2Collection(json, ArrayList.class, PlayerSimpleVo.class);
        SimplePlayerQueryResult result = new SimplePlayerQueryResult();
        result.setVos(vos);
        return result;
    }

    private String httpGet(String url, Object... uriVariables) {
        ResponseEntity<String> result = restTemplate.getForEntity(url, String.class, uriVariables);
        return result.getBody();
    }

}
