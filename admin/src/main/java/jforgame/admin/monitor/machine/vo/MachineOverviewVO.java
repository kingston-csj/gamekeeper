package jforgame.admin.monitor.machine.vo;

import lombok.Data;

import java.util.List;

@Data
public class MachineOverviewVO {

    private List<MachineRiskItemVO> riskList;

    private List<MachineMetricCardVO> summaryCards;

    private int totalCount;

    private int highCount;

    private int mediumCount;

    private int lowCount;
}
