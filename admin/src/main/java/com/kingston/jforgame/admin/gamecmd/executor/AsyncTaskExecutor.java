package com.kingston.jforgame.admin.gamecmd.executor;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by qizhao.liao on 2019/11/5.
 * 异步任务执行器
 */
@Component
public class AsyncTaskExecutor {

    @Async
    public void executor(AsyncTaskConstructor constructor, long taskId, int serverId) {
        constructor.async(serverId);
    }
}
