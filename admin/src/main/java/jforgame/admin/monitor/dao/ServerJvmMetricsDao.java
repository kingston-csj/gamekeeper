package jforgame.admin.monitor.dao;

import jforgame.admin.monitor.domain.ServerJvmMetrics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ServerJvmMetricsDao extends JpaRepository<ServerJvmMetrics, Long> {

    Optional<ServerJvmMetrics> findTopByServerIdOrderByCollectTimeDesc(String serverId);

    List<ServerJvmMetrics> findByServerIdAndCollectTimeBetweenOrderByCollectTimeAsc(
            String serverId, LocalDateTime startTime, LocalDateTime endTime);
}
