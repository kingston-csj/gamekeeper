package jforgame.admin.monitor.controller;

import jforgame.admin.monitor.service.MonitorService;
import jforgame.admin.monitor.vo.ServerMonitorNode;
import jforgame.commons.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/monitor")
public class MonitorController {

    @Autowired
    private MonitorService monitorService;

    @PostMapping(value = "/update")
    public void updateNodeInfo(@RequestParam("data") String data) {
        ServerMonitorNode monitorNode = JsonUtil.string2Object(data, ServerMonitorNode.class);
        monitorService.updateMonitorInfo(monitorNode);
    }
}
