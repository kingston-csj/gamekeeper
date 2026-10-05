package jforgame.admin.asynctask.handler;

import jakarta.annotation.PreDestroy;
import jforgame.admin.asynctask.dao.AsyncTaskDao;
import jforgame.admin.asynctask.model.AsyncTaskStatus;
import jforgame.admin.asynctask.model.AsyncTaskType;
import jforgame.admin.asynctask.model.ReqReportAsyncTaskProgress;
import jforgame.admin.asynctask.service.AsyncTaskService;
import jforgame.admin.core.SpringContext;
import jforgame.admin.domain.AsyncTask;
import jforgame.admin.domain.MachineInfo;
import jforgame.admin.domain.ServerInfo;
import jforgame.admin.gamenode.service.ServerNodeService;
import jforgame.admin.http.HttpClientService;
import jforgame.admin.logger.LoggerUtil;
import jforgame.admin.machinenode.service.MachineNodeService;
import jforgame.commons.JsonUtil;
import jforgame.commons.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ServerPowerHandler implements TaskHandler {

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
        return AsyncTaskType.SERVER_POWER;
    }

    private ConcurrentLinkedQueue<Pair<String, String>> taskQueue = new ConcurrentLinkedQueue<>();

    /**
     * 更新任务的进度定时器，key为任务id
     */
    private final Map<String, ScheduledFuture<?>> progressFutures = new ConcurrentHashMap<>();

    private final ScheduledExecutorService progressScheduler = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread thread = new Thread(r);
        thread.setName("server-power-progress");
        thread.setDaemon(true);
        return thread;
    });

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
        String operation = (String) taskParams.get("action");
        String serverName = serverInfo.getName().replaceAll("服务器", "");
        httpParams.put("serverName", serverName);
        // 特殊处理更新任务
        if ("update".equals(operation)) {
            taskQueue.add(new Pair<>(task.getTaskId(), serverName));
            // 启动定时器，每隔1秒进度加10%，最高加到90%
            startProgressTimer(task.getTaskId());
            return;
        }

        httpParams.put("operation", operation);
        try {
            httpClientService.post(machineInfo.getAgentUrl() + "/api/server_power", httpParams, HashMap.class);
        } catch (IOException e) {
            LoggerUtil.error("", e);
        }

    }

    public String popupProject() {
        if (!taskQueue.isEmpty()) {
            Pair<String, String> target = taskQueue.poll();
            // 构建开始，结束进度定时器
            stopProgressTimer(target.getFirst());
            AsyncTaskService asyncTaskService = SpringContext.getBean(AsyncTaskService.class);
            ReqReportAsyncTaskProgress progress = new ReqReportAsyncTaskProgress();
            progress.setProgress(100);
            // 任务直接完成
            progress.setData("<a href=\"http://192.168.1.123:9090/job/" + target.getSecond() + "%E5%A4%96%E7%BD%91%E4%B8%80%E9%94%AE%E6%9B%B4%E6%96%B0%E9%87%8D%E5%90%AF/lastBuild/console\" target=\"_blank\">查看构建日志</a>");
            asyncTaskService.reportProgress(target.getFirst(), progress);
            return target.getSecond();
        }
        return "";
    }

    /**
     * 启动进度定时器，每隔1秒进度加10%，最高加到90%后自动停止
     */
    private void startProgressTimer(String taskId) {
        AsyncTaskService asyncTaskService = SpringContext.getBean(AsyncTaskService.class);
        AtomicInteger progress = new AtomicInteger(0);
        ScheduledFuture<?> future = progressScheduler.scheduleAtFixedRate(() -> {
            try {
                AsyncTask current = asyncTaskService.getByTaskId(taskId);
                // 任务已结束(成功/失败)则停止定时器，避免覆盖最终状态
                if (current == null
                        || AsyncTaskStatus.SUCCESS.name().equals(current.getStatus())
                        || AsyncTaskStatus.FAIL.name().equals(current.getStatus())) {
                    stopProgressTimer(taskId);
                    return;
                }
                int next = Math.min(progress.addAndGet(10), 90);
                ReqReportAsyncTaskProgress report = new ReqReportAsyncTaskProgress();
                report.setProgress(next);
                asyncTaskService.reportProgress(taskId, report);
                if (next >= 90) {
                    // 已加到最高值，停止定时器
                    stopProgressTimer(taskId);
                }
            } catch (Exception e) {
                LoggerUtil.error("", e);
                stopProgressTimer(taskId);
            }
        }, 1, 1, TimeUnit.SECONDS);
        progressFutures.put(taskId, future);
    }

    /**
     * 结束指定任务的进度定时器
     */
    private void stopProgressTimer(String taskId) {
        ScheduledFuture<?> future = progressFutures.remove(taskId);
        if (future != null && !future.isCancelled()) {
            future.cancel(false);
        }
    }

    @PreDestroy
    public void shutdown() {
        progressScheduler.shutdownNow();
        progressFutures.clear();
    }

}

