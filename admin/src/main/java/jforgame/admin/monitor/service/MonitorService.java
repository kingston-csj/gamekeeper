package jforgame.admin.monitor.service;

import jforgame.admin.monitor.vo.ServerMonitorNode;
import jforgame.commons.TimeUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class MonitorService {

    private ConcurrentMap<Integer, ServerMonitorNode> serversMonitor = new ConcurrentHashMap<>();


    /**
     * 每隔5分钟检查指标过期
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    private void checkExpired() {
        long now = System.currentTimeMillis();
        Iterator<Map.Entry<Integer, ServerMonitorNode>> it = serversMonitor.entrySet().iterator();
        while (it.hasNext()) {
            ServerMonitorNode node = it.next().getValue();
            long diff = now - node.getHeartTime();
            if (diff > 3 * TimeUtil.MILLIS_PER_MINUTE) {
                it.remove();
            }
        }
    }

    /**
     * 更新节点数据
     *
     * @param node
     */
    public void updateMonitorInfo(ServerMonitorNode node) {
        node.setHeartTime(System.currentTimeMillis());
//        logger.info("收到{}服的心跳数据 {}", node.getServerId(), node);
        serversMonitor.put(node.getServerId(), node);
    }

    public ServerMonitorNode queryMonitorInfo(int serverId) {
        return serversMonitor.get(serverId);
    }

}
