package jforgame.admin.asynctask.handler;

import jforgame.admin.asynctask.dao.AsyncTaskDao;
import jforgame.admin.asynctask.model.AsyncTaskStatus;
import jforgame.admin.asynctask.model.AsyncTaskType;
import jforgame.admin.domain.AsyncTask;
import jforgame.admin.domain.MachineInfo;
import jforgame.admin.domain.ServerInfo;
import jforgame.admin.gamenode.service.ServerNodeService;
import jforgame.admin.http.HttpClientService;
import jforgame.admin.logger.LoggerUtil;
import jforgame.admin.machinenode.service.MachineNodeService;
import jforgame.commons.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class DbCleanHandler implements TaskHandler {

    @Autowired
    private AsyncTaskDao taskDao;

    @Autowired
    private ServerNodeService serverNodeService;

    @Autowired
    private MachineNodeService machineNodeService;

    @Autowired
    HttpClientService httpClientService;

    @Override
    public AsyncTaskType supportType() {
        return AsyncTaskType.DB_CLEAN;
    }

    @Override
    public void submit(AsyncTask task) {
        Map<String, Object> taskParams = JsonUtil.string2Map(task.getTaskParam());
        if (!taskParams.containsKey("serverId")) {
            task.setStatus(AsyncTaskStatus.FAIL.name());
            taskDao.save(task);
            return;
        }
        int serverId = (Integer) taskParams.get("serverId");
        ServerInfo serverInfo = serverNodeService.getServerNodeBy(serverId);
        if (serverInfo == null) {
            task.setStatus(AsyncTaskStatus.FAIL.name());
            taskDao.save(task);
            return;
        }
        // 根据ip拿到对应的机器
        MachineInfo machineInfo = machineNodeService.getMachineNodeBy(serverInfo.getMachineId());
        if (machineInfo == null) {
            task.setStatus(AsyncTaskStatus.FAIL.name());
            taskDao.save(task);
            return;
        }
        Map<String, Object> httpParams = new HashMap<>();
        httpParams.put("taskId", task.getTaskId());
        httpParams.put("dbName", serverInfo.getName().replaceAll("服务器", ""));
        try {
            httpClientService.post(machineInfo.getAgentUrl() + "/api/mysql_clean", httpParams, HashMap.class);
        } catch (IOException e) {
            LoggerUtil.error("", e);
        }

    }
}
