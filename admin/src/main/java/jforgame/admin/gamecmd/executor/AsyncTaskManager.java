package jforgame.admin.gamecmd.executor;

import jforgame.admin.gamecmd.model.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by qizhao.liao on 2019/11/5.
 */
@Component
public class AsyncTaskManager {

    private AtomicLong id = new AtomicLong(0);

    private Map<Long, TaskInfo> taskContainer = new HashMap<>(8);

    @Autowired
    AsyncTaskExecutor asyncTaskExecutor;

    public TaskInfo initTaskInfo(List<Integer> serverIds) {
        long taskId = getTaskId();
        TaskInfo taskInfo = new TaskInfo(taskId, serverIds);
        return taskInfo;
    }

    public TaskInfo submit(AsyncTaskConstructor constructor, List<Integer> serverIds) {
        TaskInfo info = initTaskInfo(serverIds);
        long taskId = info.getId();
        // 异步发送到每个服务器
        for (Integer serverId : serverIds) {
            asyncTaskExecutor.executor(constructor, taskId, serverId);
        }
        return info;
    }

    private long getTaskId() {
        return id.incrementAndGet();
    }

    public TaskInfo getTaskInfo(long taskId) {
        return taskContainer.get(taskId);
    }

    public TaskInfo setTaskInfo(TaskInfo info) {
        return taskContainer.put(info.getId(), info);
    }
}
