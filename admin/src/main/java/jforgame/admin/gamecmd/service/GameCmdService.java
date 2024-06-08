package jforgame.admin.gamecmd.service;

import jforgame.admin.domain.ServerInfo;
import jforgame.admin.gamecmd.cmd.CmdTypes;
import jforgame.admin.gamecmd.cmd.http.HttpServerAdminCmd;
import jforgame.admin.gamecmd.executor.AsyncTaskConstructor;
import jforgame.admin.gamecmd.executor.AsyncTaskManager;
import jforgame.admin.gamecmd.model.TaskInfo;
import jforgame.admin.gamenode.service.ServerNodeService;
import jforgame.admin.logger.LoggerFunction;
import jforgame.admin.logger.LoggerUtil;
import jforgame.admin.security.JwtTokenUtils;
import jforgame.admin.security.SecurityUtils;
import jforgame.commons.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GameCmdService {

    @Autowired
    private ServerNodeService serversManager;

    @Autowired
    private AsyncTaskManager asyncTaskManager;

    public String execServerCmd(List<Integer> servers, int type, String params) {
        Class clazz = CmdTypes.queryCmd(type).getClazz();
        Map<Integer, String> result = new HashMap<>();
        for (Integer serverId : servers) {
            ServerInfo server = serversManager.getServerNodeBy(serverId);
            if (server == null) {
                continue;
            }
            try {
                Constructor constructor = clazz.getConstructor(ServerInfo.class, String.class);
                HttpServerAdminCmd adminCmd = (HttpServerAdminCmd) constructor.newInstance(server, params);
                result.put(serverId, (String) adminCmd.action());
            } catch (Exception e) {
                result.put(serverId, e.getMessage());
            }
        }
        LoggerUtil.info(LoggerFunction.ADMIN_CMD, "operator", SecurityUtils.getUsername(),"type", type, "params", params,  "result", JsonUtil.object2String(result));
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
