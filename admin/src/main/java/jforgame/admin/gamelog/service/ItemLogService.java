package jforgame.admin.gamelog.service;

import jforgame.admin.gamelog.domain.ItemLog;
import jforgame.admin.gamelog.io.ReqQueryItemLog;
import jforgame.admin.database.ClickHouseQueryTemplate;
import jforgame.admin.domain.ServerInfo;
import jforgame.admin.gamecmd.cmd.http.QueryLogReasonsCmd;
import jforgame.admin.gamenode.service.ServerNodeService;
import jforgame.admin.http.PageResult;
import jforgame.commons.NumberUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@ConditionalOnProperty(prefix = "spring.datasource.clickhouse", name = "enabled", havingValue = "true")
public class ItemLogService {

    private static final long DEFAULT_TIME_RANGE_MILLIS = 24 * 60 * 60 * 1000L;

    private static final String BASE_TABLE_SQL = " from item_log where 1 = 1";

    private static final RowMapper<ItemLog> ITEM_LOG_ROW_MAPPER = (rs, rowNum) -> {
        ItemLog itemLog = new ItemLog();
        itemLog.setTime(getLong(rs, "time"));
        itemLog.setModule(getString(rs, "module"));
        itemLog.setDate(getString(rs, "date"));
        itemLog.setPlayerId(getString(rs, "playerId"));
        itemLog.setSid(getString(rs, "sid"));
        itemLog.setReason(getString(rs, "reason"));
        itemLog.setExt(getMap(rs, "ext"));
        return itemLog;
    };

    private final ClickHouseQueryTemplate clickHouseQueryTemplate;
    private final ServerNodeService serverNodeService;

    /**
     * 第一层为serverId, 第二层为 id -> 中文描述
     */
    private Map<Integer, Map<Integer, String>> actionReasons = new ConcurrentHashMap<>();

    public ItemLogService(ClickHouseQueryTemplate clickHouseQueryTemplate, ServerNodeService serverNodeService) {
        this.clickHouseQueryTemplate = clickHouseQueryTemplate;
        this.serverNodeService = serverNodeService;
    }

    public PageResult queryPage(ReqQueryItemLog req) {
        int pageNum = Math.max(1, req.getPageNum());
        int pageSize = Math.min(Math.max(1, req.getPageSize()), 100);

        QuerySpec countQuerySpec = buildBaseQuerySpec(req);

        long total;
        int totalPages;
        if (shouldCount(req)) {
            Long totalSize = clickHouseQueryTemplate.queryForObject(
                    "select count(*)" + countQuerySpec.getWhereSql(),
                    Long.class,
                    countQuerySpec.getArgs().toArray());
            total = totalSize == null ? 0L : totalSize;
            totalPages = total == 0 ? 0 : (int) ((total + pageSize - 1) / pageSize);
        } else {
            total = req.getCachedTotalSize();
            totalPages = req.getCachedTotalPages();
        }

        CursorQuerySpec cursorQuerySpec = buildCursorQuerySpec(req, countQuerySpec);
        List<Object> queryArgs = new ArrayList<>(cursorQuerySpec.getArgs());
        queryArgs.add(pageSize + 1);

        String sql = "select time, module, `date` as date, playerId, sid, reason, ext"
                + cursorQuerySpec.getWhereSql() + cursorQuerySpec.getOrderBySql() + " limit ?";
        List<ItemLog> content = clickHouseQueryTemplate.query(
                sql,
                ITEM_LOG_ROW_MAPPER,
                queryArgs.toArray());
        boolean overflow = content.size() > pageSize;
        if (overflow) {
            content = new ArrayList<>(content.subList(0, pageSize));
        }
        if (cursorQuerySpec.isReverseResult()) {
            Collections.reverse(content);
        }
        log.info("cursor sql {} page {} direction {}", sql, pageSize, req.getCursorDirection());
        translateReasons(content);

        PageResult pageResult = new PageResult();
        pageResult.setPageNum(pageNum);
        pageResult.setPageSize(pageSize);
        pageResult.setTotalSize(total);
        pageResult.setTotalPages(totalPages);
        pageResult.setContent(content);
        return pageResult;
    }

    private boolean shouldCount(ReqQueryItemLog req) {
        return req.isNeedCount() || req.getCachedTotalSize() <= 0 || req.getCachedTotalPages() <= 0;
    }

    private void translateReasons(List<ItemLog> content) {
        for (ItemLog itemLog : content) {
            if (itemLog.getExt() == null || itemLog.getExt().isEmpty()) {
                continue;
            }
            if (!itemLog.getExt().containsKey("reason")) {
                continue;
            }
            Integer serverId = NumberUtil.intValue(itemLog.getSid());
            Map<Integer, String> reasonMap = getActionReasons(serverId);
            if (reasonMap == null || reasonMap.isEmpty()) {
                continue;
            }
            Integer reasonId = NumberUtil.intValue(itemLog.getExt().get("reason"));
            String reasonName = reasonMap.get(reasonId);
            if (StringUtils.isNotBlank(reasonName)) {
                itemLog.setReason(reasonName);
            }
        }
    }

    private Map<Integer, String> getActionReasons(int serverId) {

        Map<Integer, String> cached = actionReasons.get(serverId);
        if (cached != null && !cached.isEmpty()) {
            return cached;
        }
        try {
            ServerInfo serverNode = serverNodeService.getServerNodeBy(serverId);
            if (serverNode == null) {
                return null;
            }
            Map<Integer, String> fetched = new QueryLogReasonsCmd(serverNode).action();
            if (fetched == null || fetched.isEmpty()) {
                return null;
            }
            actionReasons.put(serverId, fetched);

            return fetched;
        } catch (Exception e) {
            return null;
        }
    }

    private QuerySpec buildBaseQuerySpec(ReqQueryItemLog req) {
        StringBuilder whereSql = new StringBuilder(BASE_TABLE_SQL);
        List<Object> args = new ArrayList<>();

        long[] normalizedTimeRange = normalizeTimeRange(req);
        appendTimeRangeCondition(whereSql, args, normalizedTimeRange[0], normalizedTimeRange[1]);
        if (StringUtils.isNotBlank(req.getModule())) {
            whereSql.append(" and module = ?");
            args.add(req.getModule().trim());
        }
        if (StringUtils.isNotBlank(req.getDate())) {
            whereSql.append(" and `date` = ?");
            args.add(req.getDate().trim());
        }
        if (StringUtils.isNotBlank(req.getPlayerId())) {
            whereSql.append(" and playerId = ?");
            args.add(req.getPlayerId().trim());
        }
        if (StringUtils.isNotBlank(req.getSid())) {
            whereSql.append(" and sid = ?");
            args.add(req.getSid().trim());
        }
        appendDynamicFilters(whereSql, args, req);
        return new QuerySpec(whereSql.toString(), args);
    }

    private CursorQuerySpec buildCursorQuerySpec(ReqQueryItemLog req, QuerySpec baseQuerySpec) {
        StringBuilder whereSql = new StringBuilder(baseQuerySpec.getWhereSql());
        List<Object> args = new ArrayList<>(baseQuerySpec.getArgs());
        boolean prevCursor = "prev".equalsIgnoreCase(req.getCursorDirection());
        appendCursorCondition(whereSql, args, req, prevCursor);
        String orderBySql = prevCursor
                ? " order by time asc, sid asc, playerId asc"
                : " order by time desc, sid desc, playerId desc";
        return new CursorQuerySpec(whereSql.toString(), args, orderBySql, prevCursor);
    }

    private void appendTimeRangeCondition(StringBuilder whereSql, List<Object> args, long startTime, long endTime) {
        whereSql.append(" and time >= ?");
        args.add(startTime);
        whereSql.append(" and time <= ?");
        args.add(endTime);

        long startSecond = startTime / 1000;
        long endSecond = endTime / 1000;
        whereSql.append(" and toDate(time / 1000) >= toDate(toDateTime(?))");
        args.add(startSecond);
        whereSql.append(" and toDate(time / 1000) <= toDate(toDateTime(?))");
        args.add(endSecond);
    }

    private long[] normalizeTimeRange(ReqQueryItemLog req) {
        long endTime = req.getEndTime() > 0 ? req.getEndTime() : System.currentTimeMillis();
        long startTime = req.getStartTime() > 0 ? req.getStartTime() : endTime - DEFAULT_TIME_RANGE_MILLIS;
        if (startTime > endTime) {
            long temp = startTime;
            startTime = endTime;
            endTime = temp;
        }
        return new long[]{startTime, endTime};
    }

    private void appendCursorCondition(StringBuilder whereSql, List<Object> args, ReqQueryItemLog req, boolean prevCursor) {
        if (req.getCursorTime() <= 0) {
            return;
        }
        if (StringUtils.isBlank(req.getCursorSid()) || StringUtils.isBlank(req.getCursorPlayerId())) {
            whereSql.append(prevCursor ? " and time > ?" : " and time < ?");
            args.add(req.getCursorTime());
            return;
        }
        if (prevCursor) {
            whereSql.append(" and (time, sid, playerId) > (?, ?, ?)");
        } else {
            whereSql.append(" and (time, sid, playerId) < (?, ?, ?)");
        }
        args.add(req.getCursorTime());
        args.add(req.getCursorSid());
        args.add(req.getCursorPlayerId());
    }

    private void appendDynamicFilters(StringBuilder whereSql, List<Object> args, ReqQueryItemLog req) {
        if (req.getFilters() == null || req.getFilters().isEmpty()) {
            return;
        }
        for (ReqQueryItemLog.FilterItem item : req.getFilters()) {
            if (item == null || StringUtils.isBlank(item.getField())) {
                continue;
            }
            String operator = normalizeOperator(item.getOperator());
            if (item.getField().startsWith("ext.")) {
                appendExtCondition(whereSql, args, item, operator);
            } else {
                appendFixedCondition(whereSql, args, item, operator);
            }
        }
    }

    private void appendExtCondition(StringBuilder whereSql, List<Object> args, ReqQueryItemLog.FilterItem item, String operator) {
        String extKey = sanitizeExtKey(item.getField().substring(4));
        if (StringUtils.isBlank(extKey)) {
            return;
        }
        String extExpr = "ext['" + extKey + "']";
        switch (operator) {
            case "between" -> {
                String[] range = parseRange(item.getRangeValue());
                if (range == null) {
                    return;
                }
                whereSql.append(" and toFloat64OrZero(").append(extExpr).append(") between ? and ?");
                args.add(range[0]);
                args.add(range[1]);
            }
            case "in" -> appendInCondition(whereSql, args, extExpr, item.getMultiValue());
            case "like" -> {
                if (StringUtils.isBlank(item.getValue())) {
                    return;
                }
                whereSql.append(" and ").append(extExpr).append(" like ?");
                args.add(item.getValue().trim());
            }
            default -> {
                if (StringUtils.isBlank(item.getValue())) {
                    return;
                }
                whereSql.append(" and ").append(extExpr).append(" ").append(operator).append(" ?");
                args.add(item.getValue().trim());
            }
        }
    }

    private void appendFixedCondition(StringBuilder whereSql, List<Object> args, ReqQueryItemLog.FilterItem item, String operator) {
        String field = sanitizeFixedField(item.getField());
        if (field == null) {
            return;
        }
        switch (operator) {
            case "between" -> {
                String[] range = parseRange(item.getRangeValue());
                if (range == null) {
                    return;
                }
                whereSql.append(" and ").append(field).append(" between ? and ?");
                args.add(range[0]);
                args.add(range[1]);
            }
            case "in" -> appendInCondition(whereSql, args, field, item.getMultiValue());
            case "like" -> {
                if (StringUtils.isBlank(item.getValue())) {
                    return;
                }
                whereSql.append(" and ").append(field).append(" like ?");
                args.add(item.getValue().trim());
            }
            default -> {
                if (StringUtils.isBlank(item.getValue())) {
                    return;
                }
                whereSql.append(" and ").append(field).append(" ").append(operator).append(" ?");
                args.add(item.getValue().trim());
            }
        }
    }

    private void appendInCondition(StringBuilder whereSql, List<Object> args, String fieldExpr, String multiValue) {
        String[] values = parseMultiValue(multiValue);
        if (values.length == 0) {
            return;
        }
        whereSql.append(" and ").append(fieldExpr).append(" in (");
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                whereSql.append(", ");
            }
            whereSql.append("?");
            args.add(values[i]);
        }
        whereSql.append(")");
    }

    private String normalizeOperator(String operator) {
        if (StringUtils.isBlank(operator)) {
            return "=";
        }
        String normalized = operator.trim().toLowerCase();
        return switch (normalized) {
            case "=", "!=", ">", "<", ">=", "<=", "between", "in", "like" -> normalized;
            default -> "=";
        };
    }

    private String sanitizeExtKey(String extKey) {
        if (StringUtils.isBlank(extKey)) {
            return null;
        }
        String normalized = extKey.trim();
        return normalized.matches("[A-Za-z0-9_]+") ? normalized : null;
    }

    private String sanitizeFixedField(String field) {
        if (StringUtils.isBlank(field)) {
            return null;
        }
        return switch (field.trim()) {
            case "time" -> "time";
            case "module" -> "module";
            case "date" -> "`date`";
            case "playerId" -> "playerId";
            case "sid" -> "sid";
            case "reason" -> "reason";
            default -> null;
        };
    }

    private String[] parseRange(String rangeValue) {
        if (StringUtils.isBlank(rangeValue)) {
            return null;
        }
        String[] parts = rangeValue.split(",", 2);
        if (parts.length != 2 || StringUtils.isBlank(parts[0]) || StringUtils.isBlank(parts[1])) {
            return null;
        }
        return new String[]{parts[0].trim(), parts[1].trim()};
    }

    private String[] parseMultiValue(String multiValue) {
        if (StringUtils.isBlank(multiValue)) {
            return new String[0];
        }
        return java.util.Arrays.stream(multiValue.split(","))
                .map(String::trim)
                .filter(StringUtils::isNotBlank)
                .toArray(String[]::new);
    }

    private static Long getLong(ResultSet rs, String columnLabel) throws SQLException {
        long value = rs.getLong(columnLabel);
        return rs.wasNull() ? null : value;
    }

    private static String getString(ResultSet rs, String columnLabel) throws SQLException {
        return rs.getString(columnLabel);
    }

    @SuppressWarnings("unchecked")
    private static Map<String, String> getMap(ResultSet rs, String columnLabel) throws SQLException {
        Object value = rs.getObject(columnLabel);
        if (!(value instanceof Map<?, ?> mapValue)) {
            return null;
        }
        Map<String, String> result = new LinkedHashMap<>();
        for (Map.Entry<?, ?> entry : mapValue.entrySet()) {
            result.put(String.valueOf(entry.getKey()), entry.getValue() == null ? null : String.valueOf(entry.getValue()));
        }
        return result;
    }

    private static class QuerySpec {
        private final String whereSql;
        private final List<Object> args;

        private QuerySpec(String whereSql, List<Object> args) {
            this.whereSql = whereSql;
            this.args = args;
        }

        public String getWhereSql() {
            return whereSql;
        }

        public List<Object> getArgs() {
            return args;
        }
    }

    private static class CursorQuerySpec extends QuerySpec {
        private final String orderBySql;
        private final boolean reverseResult;

        private CursorQuerySpec(String whereSql, List<Object> args, String orderBySql, boolean reverseResult) {
            super(whereSql, args);
            this.orderBySql = orderBySql;
            this.reverseResult = reverseResult;
        }

        public String getOrderBySql() {
            return orderBySql;
        }

        public boolean isReverseResult() {
            return reverseResult;
        }
    }
}
