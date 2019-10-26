package com.kingston.jforgame.admin.monitor.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.kingston.jforgame.admin.monitor.service.MonitorService;
import com.kingston.jforgame.admin.monitor.vo.ServerMonitorNode;
import com.kingston.jforgame.admin.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/monitor")
public class MonitorController {

    @Autowired
    private MonitorService monitorService;

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public void updateNodeInfo(@RequestParam("data") String data) {
        ServerMonitorNode monitorNode = JsonUtil.string2Object(data, ServerMonitorNode.class);
        monitorService.updateMonitorInfo(monitorNode);
    }
}
