package jforgame.admin.monitor.vo;

import jforgame.admin.monitor.domain.ServerJvmMetrics;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ServerJvmSnapshotDto {

    private static final double BYTES_PER_MB = 1024.0 * 1024.0;

    private long heapUsed;

    private long heapCommitted;

    private long heapMax;

    private long directUsed;

    private double processCpuPercent;

    private int onlineCount;

    public static ServerJvmMetrics toEntity(String serverId, ServerJvmSnapshotDto dto) {
        ServerJvmMetrics entity = new ServerJvmMetrics();
        entity.setServerId(serverId);
        entity.setHeapUsed(toMB(dto.heapUsed));
        entity.setHeapCommitted(toMB(dto.heapCommitted));
        entity.setHeapMax(toMB(dto.heapMax));
        entity.setDirectUsed(toMB(dto.directUsed));
        entity.setProcessCpuPercent((float) dto.processCpuPercent);
        entity.setOnlineCount(dto.onlineCount);
        entity.setCollectTime(LocalDateTime.now());
        return entity;
    }

    private static Float toMB(long bytes) {
        if (bytes < 0) {
            return 0f;
        }
        return (float) (bytes / BYTES_PER_MB);
    }
}
