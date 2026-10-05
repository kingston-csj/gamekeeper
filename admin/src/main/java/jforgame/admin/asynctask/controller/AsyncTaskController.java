package jforgame.admin.asynctask.controller;

import jforgame.admin.asynctask.model.ReqReportAsyncTaskProgress;
import jforgame.admin.asynctask.model.ReqSubmitAsyncTask;
import jforgame.admin.asynctask.service.AsyncTaskService;
import jforgame.admin.http.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/async-task")
public class AsyncTaskController {

    @Autowired
    private AsyncTaskService asyncTaskService;

    @PostMapping("/submit")
    public HttpResult submit(@RequestBody ReqSubmitAsyncTask req) {
        try {
            return HttpResult.ok(asyncTaskService.submit(req));
        } catch (Exception e) {
            return HttpResult.error(e.getMessage());
        }
    }

    @GetMapping("/list")
    public HttpResult list(
            @RequestParam(required = false) String taskType,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        try {
            return HttpResult.ok(asyncTaskService.findPage(taskType, status, pageNum, pageSize));
        } catch (Exception e) {
            return HttpResult.error(e.getMessage());
        }
    }

    @GetMapping("/{taskId}")
    public HttpResult detail(@PathVariable("taskId") String taskId) {
        try {
            return HttpResult.ok(asyncTaskService.getByTaskId(taskId));
        } catch (Exception e) {
            return HttpResult.error(e.getMessage());
        }
    }

    @PostMapping("/{taskId}/progress")
    public HttpResult reportProgress(
            @PathVariable("taskId") String taskId,
            @RequestBody ReqReportAsyncTaskProgress req
    ) {
        try {
            return HttpResult.ok(asyncTaskService.reportProgress(taskId, req));
        } catch (Exception e) {
            return HttpResult.error(e.getMessage());
        }
    }
}
