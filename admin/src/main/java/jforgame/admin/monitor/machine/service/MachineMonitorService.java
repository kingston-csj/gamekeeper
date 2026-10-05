package jforgame.admin.monitor.machine.service;

import jforgame.admin.database.ClickHouseQueryTemplate;
import jforgame.admin.domain.MachineInfo;
import jforgame.admin.machinenode.dao.MachineInfoDao;
import jforgame.admin.machinenode.io.MachineNodeInfo;
import jforgame.admin.machinenode.service.MachineNodeService;
import jforgame.admin.monitor.machine.domain.MachineMonitorMetrics;
import jforgame.admin.monitor.machine.vo.MachineMetricCardVO;
import jforgame.admin.monitor.machine.vo.MachineOverviewVO;
import jforgame.admin.monitor.machine.vo.MachineRiskItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
// @ConditionalOnProperty(prefix = "spring.datasource.clickhouse", name = "enabled", havingValue = "true")
public class MachineMonitorService {

    private static final ZoneId SHANGHAI_ZONE = ZoneId.of("Asia/Shanghai");
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final float CPU_WARN = 70f;
    private static final float CPU_DANGER = 85f;
    private static final float MEM_WARN = 75f;
    private static final float MEM_DANGER = 90f;
    private static final float DISK_WARN = 80f;
    private static final float DISK_DANGER = 90f;
    private static final float LOAD_WARN = 2f;
    private static final float LOAD_DANGER = 5f;

    @Autowired
    MachineNodeService machineNodeService;

    private static final String INSERT_MONITOR_METRICS_SQL = "insert into monitor_metrics "
            + "(create_time, host_ip, host_name, cpu_usage, mem_usage, disk_usage, mem_total, mem_used, disk_total, disk_used, load_1, load_5, load_15) "
            + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String QUERY_LATEST_METRICS_SQL = "select "
            + "host_ip, "
            + "argMax(host_name, metric_time) as host_name, "
            + "argMax(cpu_usage, metric_time) as cpu_usage, "
            + "argMax(mem_usage, metric_time) as mem_usage, "
            + "argMax(disk_usage, metric_time) as disk_usage, "
            + "argMax(mem_total, metric_time) as mem_total, "
            + "argMax(mem_used, metric_time) as mem_used, "
            + "argMax(disk_total, metric_time) as disk_total, "
            + "argMax(disk_used, metric_time) as disk_used, "
            + "argMax(load_1, metric_time) as load_1, "
            + "argMax(load_5, metric_time) as load_5, "
            + "argMax(load_15, metric_time) as load_15, "
            + "max(metric_time) as create_time "
            + "from (select create_time as metric_time, host_ip, host_name, cpu_usage, mem_usage, disk_usage, mem_total, mem_used, disk_total, disk_used, load_1, load_5, load_15 from monitor_metrics) t "
            + "group by host_ip";

    private static final String QUERY_HISTORY_METRICS_SQL = "select "
            + "create_time, host_ip, host_name, cpu_usage, mem_usage, disk_usage, mem_total, mem_used, disk_total, disk_used, load_1, load_5, load_15 "
            + "from monitor_metrics where host_ip = ? and create_time >= ? and create_time <= ? order by create_time asc";

    private static final org.springframework.jdbc.core.RowMapper<MachineMonitorMetrics> MACHINE_METRICS_ROW_MAPPER = (rs, rowNum) -> {
        MachineMonitorMetrics metrics = new MachineMonitorMetrics();
        Timestamp timestamp = rs.getTimestamp("create_time");
        metrics.setCreateTime(timestamp == null ? null : timestamp.toLocalDateTime());
        metrics.setHostIp(rs.getString("host_ip"));
        metrics.setHostName(rs.getString("host_name"));
        metrics.setCpuUsage(getFloat(rs, "cpu_usage"));
        metrics.setMemUsage(getFloat(rs, "mem_usage"));
        metrics.setDiskUsage(getFloat(rs, "disk_usage"));
        metrics.setMemTotal(getLong(rs, "mem_total"));
        metrics.setMemUsed(getLong(rs, "mem_used"));
        metrics.setDiskTotal(getLong(rs, "disk_total"));
        metrics.setDiskUsed(getLong(rs, "disk_used"));
        metrics.setLoad1(getFloat(rs, "load_1"));
        metrics.setLoad5(getFloat(rs, "load_5"));
        metrics.setLoad15(getFloat(rs, "load_15"));
        return metrics;
    };

    private final MachineInfoDao machineInfoDao;

    private final ClickHouseQueryTemplate clickHouseQueryTemplate;

    public MachineMonitorService(MachineInfoDao machineInfoDao, ClickHouseQueryTemplate clickHouseQueryTemplate) {
        this.machineInfoDao = machineInfoDao;
        this.clickHouseQueryTemplate = clickHouseQueryTemplate;
    }

    public void report(MachineMonitorMetrics metrics) {
        if (metrics == null) {
            return;
        }
        if (!isLegalIp(metrics.getHostIp())) {
            return;
        }
        MachineMonitorMetrics normalized = normalize(metrics);
        persistToClickHouse(normalized);
    }

    private boolean isLegalIp(String ip) {
        for (MachineNodeInfo server : machineNodeService.getServerNodeList().getServers()) {
            if (server.getIp().equals(ip)) {
                return true;
            }
        }
        return false;
    }

    public MachineOverviewVO getOverview() {
        Map<String, MachineMonitorMetrics> latestMetrics = loadLatestMetrics();
        List<MachineInfo> machineInfos = machineInfoDao.findAll();
        Map<String, MachineRiskItemVO> itemMap = new LinkedHashMap<>();
        for (MachineInfo machineInfo : machineInfos) {
            MachineMonitorMetrics metrics = latestMetrics.get(buildMetricKey(machineInfo.getIp(), machineInfo.getName()));
            if (metrics == null) {
                continue;
            }
            itemMap.put(buildMetricKey(machineInfo.getIp(), machineInfo.getName()), toRiskItem(machineInfo, metrics));
        }
        for (MachineMonitorMetrics metrics : latestMetrics.values()) {
            String key = buildMetricKey(metrics.getHostIp(), metrics.getHostName());
            itemMap.putIfAbsent(key, toRiskItem(null, metrics));
        }

        List<MachineRiskItemVO> riskList = new ArrayList<>(itemMap.values());
        riskList.sort(Comparator.comparing(MachineRiskItemVO::getRiskScore, Comparator.nullsLast(Comparator.reverseOrder()))
                .thenComparing(MachineRiskItemVO::getHostIp, Comparator.nullsLast(String::compareTo)));

        MachineOverviewVO overview = new MachineOverviewVO();
        overview.setRiskList(riskList);
        overview.setSummaryCards(buildSummaryCards(riskList));
        overview.setTotalCount(riskList.size());
        overview.setHighCount((int) riskList.stream().filter(item -> "high".equals(item.getRiskLevel())).count());
        overview.setMediumCount((int) riskList.stream().filter(item -> "medium".equals(item.getRiskLevel())).count());
        overview.setLowCount((int) riskList.stream().filter(item -> "low".equals(item.getRiskLevel())).count());
        return overview;
    }

    public List<MachineMonitorMetrics> findHistory(String hostIp, LocalDateTime startTime, LocalDateTime endTime) {
        if (!hasText(hostIp) || startTime == null || endTime == null) {
            return new ArrayList<>();
        }
        return clickHouseQueryTemplate.query(
                QUERY_HISTORY_METRICS_SQL,
                MACHINE_METRICS_ROW_MAPPER,
                hostIp.trim(),
                Timestamp.valueOf(startTime),
                Timestamp.valueOf(endTime));
    }

    private Map<String, MachineMonitorMetrics> loadLatestMetrics() {
        List<MachineMonitorMetrics> metricList = clickHouseQueryTemplate.query(QUERY_LATEST_METRICS_SQL, MACHINE_METRICS_ROW_MAPPER);
        Map<String, MachineMonitorMetrics> metricsMap = new LinkedHashMap<>();
        for (MachineMonitorMetrics metrics : metricList) {
            metricsMap.put(buildMetricKey(metrics.getHostIp(), metrics.getHostName()), metrics);
        }
        return metricsMap;
    }

    private List<MachineMetricCardVO> buildSummaryCards(List<MachineRiskItemVO> riskList) {
        List<MachineMetricCardVO> cards = new ArrayList<>(4);
        cards.add(buildMetricCard("cpuUsage", "CPU使用率", riskList, MachineRiskItemVO::getCpuUsage));
        cards.add(buildMetricCard("memUsage", "内存使用率", riskList, MachineRiskItemVO::getMemUsage));
        cards.add(buildMetricCard("diskUsage", "磁盘使用率", riskList, MachineRiskItemVO::getDiskUsage));
        cards.add(buildMetricCard("load1", "系统平均负载", riskList, MachineRiskItemVO::getLoad1));
        return cards;
    }

    private MachineMetricCardVO buildMetricCard(String key, String name, List<MachineRiskItemVO> riskList,
                                                java.util.function.Function<MachineRiskItemVO, Float> extractor) {
        MachineRiskItemVO target = riskList.stream()
                .filter(item -> extractor.apply(item) != null)
                .max(Comparator.comparing(extractor))
                .orElse(null);
        MachineMetricCardVO card = new MachineMetricCardVO();
        card.setMetricKey(key);
        card.setMetricName(name);
        if (target == null) {
            card.setValue(0f);
            card.setHostIp("-");
            card.setHostName("-");
            card.setUpdateTime("-");
            card.setLevel("low");
            return card;
        }
        Float value = extractor.apply(target);
        card.setValue(round(value));
        card.setHostIp(defaultText(target.getHostIp()));
        card.setHostName(defaultText(target.getHostName()));
        card.setUpdateTime(defaultText(target.getUpdateTime()));
        card.setLevel(metricLevel(key, value));
        return card;
    }

    private MachineRiskItemVO toRiskItem(MachineInfo machineInfo, MachineMonitorMetrics metrics) {
        MachineRiskItemVO item = new MachineRiskItemVO();
        String hostIp = metrics != null && hasText(metrics.getHostIp()) ? metrics.getHostIp() : machineInfo != null ? machineInfo.getIp() : "";
        String hostName = metrics != null && hasText(metrics.getHostName()) ? metrics.getHostName() : machineInfo != null ? machineInfo.getName() : "";
        item.setHostIp(hostIp);
        item.setHostName(hostName);

        if (metrics == null) {
            item.setCpuUsage(0f);
            item.setMemUsage(0f);
            item.setDiskUsage(0f);
            item.setLoad1(0f);
            item.setUpdateTime("-");
            item.setRiskScore(100f);
            item.setRiskLevel("high");
            return item;
        }

        item.setCpuUsage(round(metrics.getCpuUsage()));
        item.setMemUsage(round(metrics.getMemUsage()));
        item.setDiskUsage(round(metrics.getDiskUsage()));
        item.setLoad1(round(metrics.getLoad1()));
        item.setUpdateTime(formatTime(metrics.getCreateTime()));

        float riskScore = calculateRisk(metrics);
        item.setRiskScore(riskScore);
        item.setRiskLevel(riskLevel(riskScore));
        return item;
    }

    private float calculateRisk(MachineMonitorMetrics metrics) {
        if (metrics == null) {
            return 100f;
        }
        float cpu = percent(metrics.getCpuUsage());
        float mem = percent(metrics.getMemUsage());
        float disk = percent(metrics.getDiskUsage());
        float load = normalizeLoad(metrics.getLoad1());
        float baseScore = cpu * 0.3f + mem * 0.3f + disk * 0.25f + load * 0.15f;
        long minutes = metrics.getCreateTime() == null ? Long.MAX_VALUE : Duration.between(metrics.getCreateTime(), LocalDateTime.now()).toMinutes();
        if (minutes >= 30) {
            baseScore = Math.max(baseScore, 95f);
        } else if (minutes >= 15) {
            baseScore = Math.min(100f, baseScore + 25f);
        } else if (minutes >= 5) {
            baseScore = Math.min(100f, baseScore + 10f);
        }
        return round(baseScore);
    }

    private void persistToClickHouse(MachineMonitorMetrics metrics) {
        clickHouseQueryTemplate.update(
                INSERT_MONITOR_METRICS_SQL,
                metrics.getCreateTime(),
                metrics.getHostIp(),
                metrics.getHostName(),
                metrics.getCpuUsage(),
                metrics.getMemUsage(),
                metrics.getDiskUsage(),
                metrics.getMemTotal(),
                metrics.getMemUsed(),
                metrics.getDiskTotal(),
                metrics.getDiskUsed(),
                metrics.getLoad1(),
                metrics.getLoad5(),
                metrics.getLoad15());
    }

    private MachineMonitorMetrics normalize(MachineMonitorMetrics metrics) {
        MachineMonitorMetrics normalized = new MachineMonitorMetrics();
        normalized.setCreateTime(LocalDateTime.now(SHANGHAI_ZONE));
        normalized.setHostIp(defaultText(metrics.getHostIp()));
        normalized.setHostName(defaultText(metrics.getHostName()));
        normalized.setCpuUsage(round(metrics.getCpuUsage()));
        normalized.setMemUsage(round(metrics.getMemUsage()));
        normalized.setDiskUsage(round(metrics.getDiskUsage()));
        normalized.setMemTotal(metrics.getMemTotal());
        normalized.setMemUsed(metrics.getMemUsed());
        normalized.setDiskTotal(metrics.getDiskTotal());
        normalized.setDiskUsed(metrics.getDiskUsed());
        normalized.setLoad1(round(metrics.getLoad1()));
        normalized.setLoad5(round(metrics.getLoad5()));
        normalized.setLoad15(round(metrics.getLoad15()));
        return normalized;
    }

    private String metricLevel(String key, Float value) {
        float number = value == null ? 0f : value;
        if ("cpuUsage".equals(key)) {
            if (number >= CPU_DANGER) {
                return "high";
            }
            if (number >= CPU_WARN) {
                return "medium";
            }
            return "low";
        }
        if ("memUsage".equals(key)) {
            if (number >= MEM_DANGER) {
                return "high";
            }
            if (number >= MEM_WARN) {
                return "medium";
            }
            return "low";
        }
        if ("diskUsage".equals(key)) {
            if (number >= DISK_DANGER) {
                return "high";
            }
            if (number >= DISK_WARN) {
                return "medium";
            }
            return "low";
        }
        if (number >= LOAD_DANGER) {
            return "high";
        }
        if (number >= LOAD_WARN) {
            return "medium";
        }
        return "low";
    }

    private String riskLevel(float riskScore) {
        if (riskScore >= 80f) {
            return "high";
        }
        if (riskScore >= 60f) {
            return "medium";
        }
        return "low";
    }

    private float normalizeLoad(Float load1) {
        if (load1 == null) {
            return 0f;
        }
        return round(Math.min(100f, load1 * 20f));
    }

    private float percent(Float value) {
        if (value == null) {
            return 0f;
        }
        return round(Math.max(0f, Math.min(100f, value)));
    }

    private String formatTime(LocalDateTime time) {
        return time == null ? "-" : time.format(DTF);
    }

    private String buildMetricKey(String hostIp, String hostName) {
        if (hasText(hostIp)) {
            return hostIp.trim();
        }
        if (hasText(hostName)) {
            return hostName.trim();
        }
        return "unknown";
    }

    private boolean hasText(String text) {
        return text != null && !text.trim().isEmpty();
    }

    private String defaultText(String text) {
        return hasText(text) ? text.trim() : "";
    }

    private Float round(Float value) {
        if (value == null) {
            return null;
        }
        return Math.round(value * 100) / 100f;
    }

    private static Float getFloat(ResultSet rs, String columnLabel) throws SQLException {
        float value = rs.getFloat(columnLabel);
        return rs.wasNull() ? null : value;
    }

    private static Long getLong(ResultSet rs, String columnLabel) throws SQLException {
        long value = rs.getLong(columnLabel);
        return rs.wasNull() ? null : value;
    }
}
