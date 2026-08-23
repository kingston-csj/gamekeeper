package jforgame.admin.monitor.controller;

import jforgame.admin.http.HttpResult;
import jforgame.admin.monitor.service.MonitorService;
import jforgame.admin.monitor.vo.ServerJvmMetricsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/monitor")
public class MonitorController {

    @Autowired
    private MonitorService monitorService;

    @GetMapping(value = "/latest")
    public HttpResult latest(@RequestParam("serverId") String serverId) {
        ServerJvmMetricsVO latest = monitorService.findLatest(serverId);
        return HttpResult.ok(latest);
    }

    @GetMapping(value = "/history")
    public HttpResult history(
            @RequestParam("serverId") String serverId,
            @RequestParam("startTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam("endTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        List<ServerJvmMetricsVO> list = monitorService.findByRange(serverId, startTime, endTime);
        Map<String, Object> data = new HashMap<>();
        data.put("list", list);
        return HttpResult.ok(data);
    }
}
