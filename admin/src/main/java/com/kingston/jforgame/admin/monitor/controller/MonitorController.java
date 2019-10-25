package com.kingston.jforgame.admin.monitor.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/monitor")
public class MonitorController {


    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public void updateNodeInfo(@RequestBody String param) {


    }
}
