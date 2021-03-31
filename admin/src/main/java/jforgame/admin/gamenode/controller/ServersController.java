package jforgame.admin.gamenode.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jforgame.admin.domain.SysDict;
import jforgame.admin.gamenode.vo.ServerNodeInfo;
import jforgame.admin.http.HttpResult;
import jforgame.admin.http.PageRequest;
import jforgame.admin.http.PageResult;
import jforgame.admin.monitor.service.MonitorService;
import jforgame.admin.monitor.vo.ServerMonitorNode;
import jforgame.admin.domain.ServerInfo;
import jforgame.admin.gamenode.service.ServerNodeService;
import jforgame.admin.gamenode.vo.ServerNodeInfoList;
import jforgame.admin.system.vo.SysDictVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/server")
public class ServersController {

    @Autowired
    private ServerNodeService serversManager;
    @Autowired
    private MonitorService monitorService;

    @RequestMapping(value = "/findPage", method = RequestMethod.GET)
    public HttpResult getServerNodesList(PageRequest request) {
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
            vo.setName(String.format("%s(%d区)", server.getName(), server.getId()));
            vo.setIp(server.getIp());
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


        int page = Math.abs(request.getPageNum());
        int pageSize = request.getPageSize();
        pageSize = Math.abs(pageSize);
        pageSize = Math.min(pageSize, 100);
        Pageable pageRequest = org.springframework.data.domain.PageRequest.of(page - 1, pageSize);

        PageResult pageResult = new PageResult();
        pageResult.setPageNum(page);
        pageResult.setPageSize(pageSize);
        pageResult.setTotalPages(1);
        pageResult.setContent(vos);

        return HttpResult.ok(pageResult);
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
