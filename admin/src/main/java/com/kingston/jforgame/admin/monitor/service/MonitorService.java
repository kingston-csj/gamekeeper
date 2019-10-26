package com.kingston.jforgame.admin.monitor.service;

import com.kingston.jforgame.admin.monitor.vo.ServerMonitorNode;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class MonitorService {

    private ConcurrentMap<Integer, ServerMonitorNode> serversMonitor = new ConcurrentHashMap<>();

    /**
     * 更新节点数据
     * @param node
     */
    public void updateMonitorInfo(ServerMonitorNode node) {
        serversMonitor.put(node.getServerId(), node);
    }

}
