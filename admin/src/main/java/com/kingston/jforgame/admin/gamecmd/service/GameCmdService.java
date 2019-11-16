package com.kingston.jforgame.admin.gamecmd.service;

import com.kingston.jforgame.admin.domain.ServerInfo;
import com.kingston.jforgame.admin.gamecmd.cmd.CmdTypes;
import com.kingston.jforgame.admin.gamecmd.cmd.http.HotSwapCmd;
import com.kingston.jforgame.admin.gamecmd.cmd.http.HttpServerAdminCmd;
import com.kingston.jforgame.admin.gamecmd.executor.AsyncTaskConstructor;
import com.kingston.jforgame.admin.gamecmd.executor.AsyncTaskManager;
import com.kingston.jforgame.admin.gamecmd.executor.SpringTaskExecutor;
import com.kingston.jforgame.admin.gamecmd.model.TaskInfo;
import com.kingston.jforgame.admin.gamecmd.vo.PlayerSimpleVo;
import com.kingston.jforgame.admin.gamecmd.vo.SimplePlayerQueryResult;
import com.kingston.jforgame.admin.gamenode.service.ServerNodeService;
import com.kingston.jforgame.admin.security.SecurityUtils;
import com.kingston.jforgame.admin.utils.JsonUtil;
import com.kingston.jforgame.admin.utils.SimplyReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

@Service
public class GameCmdService {

    @Autowired
    private ServerNodeService serversManager;

    @Autowired
    private AsyncTaskManager asyncTaskManager;

    public String execServerCmd(List<Integer> servers, int type, String params) {
        Map<Integer, String> result = new HashMap<>();
        for (Integer serverId : servers) {
            ServerInfo server = serversManager.getServerNodeBy(serverId);
            if (server == null) {
                continue;
            }
            try {
                Class clazz = CmdTypes.queryCmd(type).getClazz();
                Constructor constructor = clazz.getConstructor(ServerInfo.class, String.class);
                HttpServerAdminCmd adminCmd = (HttpServerAdminCmd) constructor.newInstance(server, params);
                result.put(serverId, (String) adminCmd.action());
            } catch (Exception e) {
                result.put(serverId, e.getMessage());
            }
        }
        return result.toString();
    }

    /**
     * 异步多线程执行
     * @param serverIds
     * @return
     */
    public String asyncHotSwap(List<Integer> serverIds) {
        TaskInfo taskInfo = asyncTaskManager.submit(new AsyncTaskConstructor() {
            @Override
            public void async(int serverId) {
                ServerInfo server = serversManager.getServerNodeBy(serverId);
                String url = String.format("http://localhost:%s/serverController/hotSwap", server.getHttpPort());
//                String info = httpGet(url);
            }
        }, serverIds);
        return JsonUtil.object2String(taskInfo);
    }

}
