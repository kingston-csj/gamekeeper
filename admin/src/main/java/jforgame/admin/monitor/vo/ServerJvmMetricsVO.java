package jforgame.admin.monitor.vo;

import jforgame.admin.monitor.domain.ServerJvmMetrics;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class ServerJvmMetricsVO {

    public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(PATTERN_DATETIME);

    private Long id;

    private String serverId;

    private Float heapUsed;

    private Float heapCommitted;

    private Float heapMax;

    private Float directUsed;

    private Float processCpuPercent;

    private Integer onlineCount;

    private String collectTime;

    private Float heapUsageRate;

    public static LocalDateTime parseDateTime(String str) {
        if (str == null || str.isBlank()) {
            return null;
        }
        return LocalDateTime.parse(str, FORMATTER);
    }

    public static ServerJvmMetrics toEntity(ServerJvmMetricsVO vo) {
        ServerJvmMetrics entity = new ServerJvmMetrics();
        entity.setServerId(vo.getServerId());
        entity.setHeapUsed(vo.getHeapUsed());
        entity.setHeapCommitted(vo.getHeapCommitted());
        entity.setHeapMax(vo.getHeapMax());
        entity.setDirectUsed(vo.getDirectUsed());
        entity.setProcessCpuPercent(vo.getProcessCpuPercent());
        entity.setOnlineCount(vo.getOnlineCount());
        entity.setCollectTime(parseDateTime(vo.getCollectTime()));
        return entity;
    }
}
