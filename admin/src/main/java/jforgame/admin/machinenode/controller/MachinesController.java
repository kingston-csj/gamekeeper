package jforgame.admin.machinenode.controller;

import jforgame.admin.core.I18nConstants;
import jforgame.admin.domain.MachineInfo;
import jforgame.admin.http.HttpResult;
import jforgame.admin.logger.LoggerFunction;
import jforgame.admin.logger.LoggerUtil;
import jforgame.admin.security.SecurityUtils;
import jforgame.admin.machinenode.dao.MachineInfoDao;
import jforgame.admin.machinenode.io.MachineNodeInfoList;
import jforgame.admin.machinenode.io.ReqCreateMachineNode;
import jforgame.admin.machinenode.service.MachineNodeService;
import jforgame.commons.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/machine")
@Slf4j
public class MachinesController {

    @Autowired
    private MachineNodeService serversManager;

    @Autowired
    private MachineInfoDao serverInfoDao;

    @RequestMapping(value = "/findPage", method = RequestMethod.GET)
    public HttpResult getServerNodesList() {
        MachineNodeInfoList serverList = serversManager.getServerNodeList();
        return HttpResult.ok(serverList);
    }

    @PostMapping(value = "/saveNode")
    public HttpResult saveNode(@RequestBody ReqCreateMachineNode req) {
        try {
            serversManager.saveNode(req.getId(), req.getName(), req.getIp(), req.getAgentUrl(),req.getLogRoot());
            LoggerUtil.info(LoggerFunction.SERVER_NODE, "operator", SecurityUtils.getUsername(), "type", "save", "params", JsonUtil.object2String(req));
            return HttpResult.ok();
        } catch (Exception e) {
            return HttpResult.error(e.getMessage());
        }
    }

    @DeleteMapping(value = "/deleteNode")
    public HttpResult deleteNode(@RequestParam("id") int id) {
        if (id <= 0) {
            return HttpResult.error(I18nConstants.COMMON_NOT_FOUND);
        }
        LoggerUtil.info(LoggerFunction.SERVER_NODE, "operator", SecurityUtils.getUsername(), "type", "delete", "params", id);
        serversManager.deleteNode(id);
        return HttpResult.ok();
    }


    @RequestMapping(value = "/serverIds", method = RequestMethod.GET)
    public Map<String, Object> queryServerIds() {
        Map<String, Object> result = new HashMap<>();
        List<MachineInfo> servers = serverInfoDao.findAll();
        result.put("ids", servers);
        return result;
    }


}
