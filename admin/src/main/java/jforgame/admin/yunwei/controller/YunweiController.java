package jforgame.admin.yunwei.controller;

import jforgame.admin.asynctask.dao.AsyncTaskDao;
import jforgame.admin.domain.AsyncTask;
import jforgame.admin.http.HttpResult;
import jforgame.admin.s3.OssService;
import jforgame.admin.s3.S3Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@RestController
@RequestMapping("/yunwei")
@Slf4j
public class YunweiController {

    @Autowired
    OssService ossService;

    @Autowired
    S3Client s3Client;

    @Autowired
    AsyncTaskDao taskDao;

    @RequestMapping("/backup/download")
    public ResponseEntity<ByteArrayResource> download(@RequestParam("taskId") String taskId) throws IOException {
        Optional<AsyncTask> taskOptional = taskDao.findByTaskId(taskId);
        if (!taskOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        AsyncTask asyncTask = taskOptional.get();
        try (InputStream is = s3Client.getObject(asyncTask.getUrl())) {
            byte[] bytes = is.readAllBytes();
            return toBytesResponse(bytes, asyncTask.getUrl());
        }
    }

    ResponseEntity<ByteArrayResource> toBytesResponse(byte[] data, String fileName) {
        ByteArrayResource byteData = new ByteArrayResource(data);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        return ResponseEntity.ok().headers(headers).contentLength(data.length).contentType(MediaType.APPLICATION_OCTET_STREAM).body(byteData);
    }

    @DeleteMapping("/backup/delete")
    public HttpResult delete(@RequestParam("taskId") String taskId) {
        Optional<AsyncTask> taskOptional = taskDao.findByTaskId(taskId);
        if (!taskOptional.isPresent()) {
            return HttpResult.error("任务不存在");
        }
        AsyncTask asyncTask = taskOptional.get();
        s3Client.remove(asyncTask.getUrl());
        taskDao.delete(asyncTask);
        return HttpResult.ok("删除成功");
    }
}
