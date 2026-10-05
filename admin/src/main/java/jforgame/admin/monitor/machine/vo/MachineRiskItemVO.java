package jforgame.admin.monitor.machine.vo;

import lombok.Data;

@Data
public class MachineRiskItemVO {

    private String riskLevel;

    private Float riskScore;

    private String hostIp;

    private String hostName;

    private Float cpuUsage;

    private Float memUsage;

    private Float diskUsage;

    private Float load1;

    private String updateTime;
}
