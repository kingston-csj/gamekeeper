package jforgame.admin.monitor.service;

import jforgame.admin.monitor.dao.ServerJvmMetricsDao;
import jforgame.admin.monitor.domain.ServerJvmMetrics;
import jforgame.admin.monitor.vo.ServerJvmMetricsVO;
import jforgame.admin.monitor.vo.ServerJvmSnapshotDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MonitorService {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private ServerJvmMetricsDao serverJvmMetricsDao;

    public void updateNodeInfo(String serverId, ServerJvmSnapshotDto dto) {
        if (dto == null) {
            return;
        }
        serverJvmMetricsDao.save(ServerJvmSnapshotDto.toEntity(serverId, dto));
    }

    public ServerJvmMetricsVO findLatest(String serverId) {
        return serverJvmMetricsDao.findTopByServerIdOrderByCollectTimeDesc(serverId)
                .map(this::toVO)
                .orElse(null);
    }

    public List<ServerJvmMetricsVO> findByRange(String serverId, LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            return Collections.emptyList();
        }
        List<ServerJvmMetrics> list = serverJvmMetricsDao
                .findByServerIdAndCollectTimeBetweenOrderByCollectTimeAsc(serverId, startTime, endTime);
        List<ServerJvmMetricsVO> result = new ArrayList<>(list.size());
        for (ServerJvmMetrics m : list) {
            result.add(toVO(m));
        }
        return result;
    }

    private ServerJvmMetricsVO toVO(ServerJvmMetrics entity) {
        ServerJvmMetricsVO vo = new ServerJvmMetricsVO();
        vo.setId(entity.getId());
        vo.setServerId(entity.getServerId());
        vo.setHeapUsed(entity.getHeapUsed());
        vo.setHeapCommitted(entity.getHeapCommitted());
        vo.setHeapMax(entity.getHeapMax());
        vo.setDirectUsed(entity.getDirectUsed());
        vo.setProcessCpuPercent(entity.getProcessCpuPercent());
        vo.setOnlineCount(entity.getOnlineCount());
        vo.setCollectTime(entity.getCollectTime() != null ? entity.getCollectTime().format(DTF) : "");
        Float heapMax = entity.getHeapMax();
        Float heapUsed = entity.getHeapUsed();
        if (heapMax != null && heapMax > 0 && heapUsed != null) {
            float rate = heapUsed / heapMax * 100;
            vo.setHeapUsageRate(Math.round(rate * 100) / 100f);
        } else {
            vo.setHeapUsageRate(0f);
        }
        return vo;
    }
}
