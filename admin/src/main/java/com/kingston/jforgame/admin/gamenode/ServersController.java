package com.kingston.jforgame.admin.gamenode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kingston.jforgame.admin.gamenode.vo.ServerNodeInfo;
import com.kingston.jforgame.admin.monitor.service.MonitorService;
import com.kingston.jforgame.admin.monitor.vo.ServerMonitorNode;
import com.kingston.jforgame.admin.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kingston.jforgame.admin.domain.ServerInfo;
import com.kingston.jforgame.admin.gamenode.service.ServersManager;
import com.kingston.jforgame.admin.gamenode.vo.ServerNodeInfoList;

@RestController
@RequestMapping("/server")
public class ServersController {

    @Autowired
    private ServersManager serversManager;
    @Autowired
    private MonitorService monitorService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public @ResponseBody
    ServerNodeInfoList getArticleByState(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                         @RequestParam(value = "count", defaultValue = "10") Integer count) {
        ServerNodeInfoList serverList = new ServerNodeInfoList();
        int totalCount = serversManager.getServerNodeSum();
        List<ServerInfo> servers = serversManager.getServerNodeList(page, count);
        List<ServerNodeInfo> vos = new ArrayList<>(servers.size());
        for (ServerInfo server : servers) {
            ServerNodeInfo vo = new ServerNodeInfo();
            vo.setId(server.getId());
            vo.setTitle(server.getTitle());
            ServerMonitorNode monitorNode = monitorService.queryMonitorInfo(server.getId());
            if (monitorNode != null) {
                vo.setOnlinePlayerSum(monitorNode.getOnlinePlayerSum());
                vo.setCachePlayerSum(monitorNode.getCachePlayerSum());
            }
            vos.add(vo);
        }
        serverList.setTotalCount(totalCount);
        serverList.setServers(vos);

        return serverList;
    }

    @RequestMapping(value = "/serverIds", method = RequestMethod.GET)
    public Map<String, Object> queryServerIds() {
        Map<String, Object> result = new HashMap<String, Object>();
        List<Integer> ids = new ArrayList<>();
        List<ServerInfo> servers = serversManager.getServerNodeList(1, Integer.MAX_VALUE);

        servers.forEach(server -> {
            ids.add(server.getId());
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
