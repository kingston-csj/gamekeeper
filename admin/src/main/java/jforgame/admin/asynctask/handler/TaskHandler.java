package jforgame.admin.asynctask.handler;

import jforgame.admin.asynctask.model.AsyncTaskType;
import jforgame.admin.domain.AsyncTask;

/**
 * 具体任务处理器后续再补实现。
 */
public interface TaskHandler {

    AsyncTaskType supportType();

    void submit(AsyncTask task);
}
