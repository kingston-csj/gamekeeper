package jforgame.admin.asynctask.service;

import jakarta.annotation.PostConstruct;
import jforgame.admin.asynctask.dao.AsyncTaskDao;
import jforgame.admin.asynctask.handler.TaskHandler;
import jforgame.admin.asynctask.model.AsyncTaskStatus;
import jforgame.admin.asynctask.model.AsyncTaskType;
import jforgame.admin.asynctask.model.ReqReportAsyncTaskProgress;
import jforgame.admin.asynctask.model.ReqSubmitAsyncTask;
import jforgame.admin.domain.AsyncTask;
import jforgame.admin.http.PageResult;
import jforgame.admin.utils.IdFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class AsyncTaskService {

    @Autowired
    private AsyncTaskDao asyncTaskDao;

    @Autowired
    private AsyncTaskDispatchExecutor dispatchExecutor;

    @Autowired(required = false)
    private List<TaskHandler> taskHandlers = Collections.emptyList();

    public Map<String, String> submit(ReqSubmitAsyncTask req) {
        AsyncTaskType taskType = AsyncTaskType.of(req.getTaskType());
        AsyncTask task = new AsyncTask();
        task.setTaskId(IdFactory.nextUUId());
        task.setTaskType(taskType.name());
        task.setTaskParam(normalizeTaskParam(req.getTaskParam()));
        task.setStatus(AsyncTaskStatus.PENDING.name());
        task.setProgress(0);
        task.setCreateTime(new Date());
        task.setRemark(req.getRemark());
        asyncTaskDao.save(task);

        dispatch(task);

        Map<String, String> result = new HashMap<>();
        result.put("taskId", task.getTaskId());
        return result;
    }

    public PageResult findPage(String taskType, String status, int pageNum, int pageSize) {
        int page = Math.max(pageNum, 1);
        int size = Math.max(pageSize, 1);
        Specification<AsyncTask> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (taskType != null && !taskType.trim().isEmpty()) {
                predicates.add(cb.equal(root.get("taskType"), AsyncTaskType.of(taskType).name()));
            }
            if (status != null && !status.trim().isEmpty()) {
                predicates.add(cb.equal(root.get("status"), AsyncTaskStatus.of(status).name()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Page<AsyncTask> searchResult = asyncTaskDao.findAll(
                specification,
                org.springframework.data.domain.PageRequest.of(
                        page - 1,
                        size,
                        Sort.by(Sort.Direction.DESC, "createTime", "id")
                )
        );

        PageResult pageResult = new PageResult();
        pageResult.setPageNum(page);
        pageResult.setPageSize(size);
        pageResult.setTotalSize(searchResult.getTotalElements());
        pageResult.setTotalPages(searchResult.getTotalPages());
        pageResult.setContent(searchResult.getContent());
        return pageResult;
    }

    public AsyncTask getByTaskId(String taskId) {
        return asyncTaskDao.findByTaskId(taskId)
                .orElseThrow(() -> new IllegalArgumentException("任务不存在: " + taskId));
    }

    public AsyncTask reportProgress(String taskId, ReqReportAsyncTaskProgress req) {
        Integer progress = req.getProgress();
        if (progress == null) {
            throw new IllegalArgumentException("progress不能为空");
        }
        if (progress != -1 && (progress < 0 || progress > 100)) {
            throw new IllegalArgumentException("progress必须在0~100之间，失败使用-1");
        }

        AsyncTask task = getByTaskId(taskId);
        Date now = new Date();
        if (task.getStartTime() == null) {
            task.setStartTime(now);
        }

        if (progress == -1) {
            task.setStatus(AsyncTaskStatus.FAIL.name());
            task.setProgress(-1);
            task.setEndTime(now);
            task.setMessage(req.getData());
        } else if (progress == 100) {
            task.setStatus(AsyncTaskStatus.SUCCESS.name());
            task.setProgress(100);
            task.setEndTime(now);
            task.setUrl(req.getData());
            task.setMessage(req.getData());
        } else {
            task.setStatus(AsyncTaskStatus.PROCESSING.name());
            task.setProgress(progress);
            task.setMessage(req.getData());
        }
        asyncTaskDao.save(task);
        return task;
    }

    private void dispatch(AsyncTask task) {
        Optional<TaskHandler> handlerOptional = taskHandlers.stream()
                .filter(handler -> handler.supportType().name().equals(task.getTaskType()))
                .findFirst();
        if (handlerOptional.isEmpty()) {
            return;
        }

        dispatchExecutor.execute(() -> {
            try {
                handlerOptional.get().submit(task);
            } catch (Exception e) {
                AsyncTask latest = asyncTaskDao.findByTaskId(task.getTaskId()).orElse(null);
                if (latest == null) {
                    return;
                }
                Date now = new Date();
                if (latest.getStartTime() == null) {
                    latest.setStartTime(now);
                }
                latest.setStatus(AsyncTaskStatus.FAIL.name());
                latest.setProgress(-1);
                latest.setEndTime(now);
                asyncTaskDao.save(latest);
            }
        });
    }


    private String normalizeTaskParam(String taskParam) {
        if (taskParam == null || taskParam.trim().isEmpty()) {
            return "{}";
        }
        return taskParam.trim();
    }
}
