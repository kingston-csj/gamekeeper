package jforgame.admin.gamecmd.executor;

import jforgame.admin.gamecmd.model.TaskInfo;
import jforgame.admin.gamecmd.model.TaskStatus;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by qizhao.liao on 2019/11/5.
 * 异步任务监控
 */
@Component
//@Aspect
public class AsyncTaskMonitor {
    @Autowired
    AsyncTaskManager manager;

    @Around("execution(*jforgame.admin.gamecmd.executor.AsyncTaskExecutor.*(..))")
    public void taskHandle(ProceedingJoinPoint pjp) {
        //获取taskId
        long taskId = (long)pjp.getArgs()[1];
        int serverId = (int)pjp.getArgs()[2];
        //获取任务信息
        TaskInfo taskInfo = manager.getTaskInfo(taskId);
        // todo 这里是否需要锁住？？？？？？？？？？？？？
        synchronized (taskInfo) {
            taskInfo.getServerStatusMap().put(serverId, TaskStatus.RUNNING.getStateInfo());
            manager.setTaskInfo(taskInfo);
            String result = null;
            try {
                // 请求接口返回的结果
                result = (String) pjp.proceed();
            } catch (Throwable throwable) {
                result = TaskStatus.FAILED.getStateInfo();
            }
            taskInfo.getServerStatusMap().put(serverId, result);
            taskInfo.setEndTime(System.currentTimeMillis());
            manager.setTaskInfo(taskInfo);
        }
    }

}
