package jforgame.admin.gamenode.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jforgame.admin.core.I18nConstants;
import jforgame.admin.gamenode.io.ReqCreateServerNode;
import jforgame.admin.gamenode.io.ServerNodeInfo;
import jforgame.admin.http.HttpResult;
import jforgame.admin.monitor.service.MonitorService;
import jforgame.admin.monitor.vo.ServerMonitorNode;
import jforgame.admin.domain.ServerInfo;
import jforgame.admin.gamenode.service.ServerNodeService;
import jforgame.admin.gamenode.io.ServerNodeInfoList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/server")
public class ServersController {

    @Autowired
    private ServerNodeService serversManager;
    @Autowired
    private MonitorService monitorService;

    @RequestMapping(value = "/findPage", method = RequestMethod.GET)
    public HttpResult getServerNodesList() {
        ServerNodeInfoList serverList = new ServerNodeInfoList();
        int totalCount = serversManager.getServerNodeSum();
        List<ServerInfo> servers = serversManager.getServerNodeList(0, 100);
        List<ServerNodeInfo> vos = new ArrayList<>(servers.size());
        int onlineSum = 0;
        int cacheSum = 0;
        ServerNodeInfo totalVo = new ServerNodeInfo();
        totalVo.setName("总计");
        for (ServerInfo server : servers) {
            ServerNodeInfo vo = new ServerNodeInfo();
            vo.setId(server.getId());
            vo.setName(server.getName());
            vo.setIp(server.getIp());
            vo.setHttpPort(server.getHttpPort());
            ServerMonitorNode monitorNode = monitorService.queryMonitorInfo(server.getId());
            if (monitorNode != null) {
                vo.setOnlinePlayerSum(monitorNode.getOnlinePlayerSum());
                vo.setCachePlayerSum(monitorNode.getCachePlayerSum());
            }
            onlineSum += vo.getOnlinePlayerSum();
            cacheSum += vo.getCachePlayerSum();
            vos.add(vo);
        }

        totalVo.setOnlinePlayerSum(onlineSum);
        totalVo.setCachePlayerSum(cacheSum);
        vos.add(totalVo);
        serverList.setTotalCount(totalCount + 1);
        serverList.setServers(vos);
        return HttpResult.ok(serverList);
    }

    @PostMapping(value = "/saveNode")
    public HttpResult saveNode(@RequestBody ReqCreateServerNode req) {
        try {
            serversManager.saveNode(req.getId(), req.getName(), req.getIp(), req.getHttpPort());
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
        serversManager.deleteNode(id);
        return HttpResult.ok();
    }


    @RequestMapping(value = "/serverIds", method = RequestMethod.GET)
    public Map<String, Object> queryServerIds() {
        Map<String, Object> result = new HashMap<>();
        List<Integer> ids = new ArrayList<>();
        List<ServerInfo> servers = serversManager.getServerNodeList(1, Integer.MAX_VALUE);

        servers.forEach(server -> {
            if (server.getMerged() <= 0) {
                ids.add(server.getId());
            }
        });

        result.put("ids", ids);
        return result;
    }

    @GetMapping(value = "/monitor")
    public Map<String, String> serverInfo() {
        Map<String, String> result = new HashMap<>();

        result.put("userInfo", "2人");
        result.put("memory", "1g/2g");
        return result;
    }

}
