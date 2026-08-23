package jforgame.admin.monitor.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "server_jvm_metrics")
public class ServerJvmMetrics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "server_id", nullable = false, length = 64)
    private String serverId;

    @Column(name = "heap_used", nullable = false)
    private Float heapUsed;

    @Column(name = "heap_committed", nullable = false)
    private Float heapCommitted;

    @Column(name = "heap_max", nullable = false)
    private Float heapMax;

    @Column(name = "direct_used", nullable = false)
    private Float directUsed;

    @Column(name = "process_cpu_percent", nullable = false)
    private Float processCpuPercent;

    @Column(name = "online_count", nullable = false)
    private Integer onlineCount;

    @Column(name = "collect_time", nullable = false)
    private LocalDateTime collectTime;

    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;

    @PrePersist
    public void prePersist(){
        createTime = LocalDateTime.now();
    }
}
