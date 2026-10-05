package jforgame.admin.monitor.machine.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MachineMonitorMetrics {

    /**
     * 采集时间
     */
    private LocalDateTime createTime;

    /**
     * 主机IP
     */
    private String hostIp;

    /**
     * 主机名
     */
    private String hostName;

    /**
     * CPU使用率(%)
     */
    private Float cpuUsage;

    /**
     * 内存使用率(%)
     */
    private Float memUsage;

    /**
     * 磁盘使用率(%)
     */
    private Float diskUsage;

    /**
     * 内存总量(KB)
     */
    private Long memTotal;

    /**
     * 内存已用(KB)
     */
    private Long memUsed;

    /**
     * 磁盘总量(KB)
     */
    private Long diskTotal;

    /**
     * 磁盘已用(KB)
     */
    private Long diskUsed;

    /**
     * 1分钟系统平均负载
     */
    @JsonAlias({"load_1"})
    private Float load1;

    /**
     * 5分钟系统平均负载
     */
    @JsonAlias({"load_5"})
    private Float load5;

    /**
     * 15分钟系统平均负载
     */
    @JsonAlias({"load_15"})
    private Float load15;
}
