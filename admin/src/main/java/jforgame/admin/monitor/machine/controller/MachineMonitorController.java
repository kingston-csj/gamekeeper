package jforgame.admin.monitor.machine.controller;

import jakarta.servlet.http.HttpServletRequest;
import jforgame.admin.http.HttpResult;
import jforgame.admin.monitor.machine.domain.MachineMonitorMetrics;
import jforgame.admin.monitor.machine.service.MachineMonitorService;
import jforgame.admin.monitor.machine.vo.MachineOverviewVO;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@ConditionalOnProperty(prefix = "spring.datasource.clickhouse", name = "enabled", havingValue = "true")
public class MachineMonitorController {

    private final MachineMonitorService machineMonitorService;

    public MachineMonitorController(MachineMonitorService machineMonitorService) {
        this.machineMonitorService = machineMonitorService;
    }

    @PostMapping("/api/metrics/report")
    public HttpResult report(HttpServletRequest request, @RequestBody MachineMonitorMetrics metrics) {
        metrics.setHostIp(request.getRemoteAddr());
        machineMonitorService.report(metrics);
        return HttpResult.ok();
    }

    @GetMapping("/monitor/machine/overview")
    public HttpResult overview() {
        MachineOverviewVO overview = machineMonitorService.getOverview();
        return HttpResult.ok(overview);
    }

    @GetMapping("/monitor/machine/history")
    public HttpResult history(
            @RequestParam("hostIp") String hostIp,
            @RequestParam("startTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam("endTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        List<MachineMonitorMetrics> list = machineMonitorService.findHistory(hostIp, startTime, endTime);
        Map<String, Object> data = new HashMap<>();
        data.put("list", list);
        return HttpResult.ok(data);
    }
}
