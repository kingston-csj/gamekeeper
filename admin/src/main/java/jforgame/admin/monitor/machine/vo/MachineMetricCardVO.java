package jforgame.admin.monitor.machine.vo;

import lombok.Data;

@Data
public class MachineMetricCardVO {

    private String metricKey;

    private String metricName;

    private Float value;

    private String hostIp;

    private String hostName;

    private String updateTime;

    private String level;
}
