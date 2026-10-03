package jforgame.admin.gamelog.service;

import jforgame.admin.database.ClickHouseQueryTemplate;
import jforgame.admin.gamelog.domain.ServerErrorLog;
import jforgame.admin.gamelog.io.ReqQueryItemLog;
import jforgame.admin.gamenode.service.ServerNodeService;
import jforgame.admin.http.PageResult;
import jforgame.commons.NumberUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@ConditionalOnProperty(prefix = "spring.datasource.clickhouse", name = "enabled", havingValue = "true")
public class ExceptionLogService {

    private static final long DEFAULT_TIME_RANGE_MILLIS = 24 * 60 * 60 * 1000L;

    private static final String BASE_TABLE_SQL = " from server_error_log where 1 = 1";

    @Autowired
    ServerNodeService serverNodeService;

    private final RowMapper<ServerErrorLog> ERROR_LOG_ROW_MAPPER = (rs, rowNum) -> {
        ServerErrorLog errorLog = new ServerErrorLog();
        String serverId = getString(rs, "serverId");
        errorLog.setServerId(serverId);
        if (NumberUtil.intValue(serverId) > 0) {
            var node = serverNodeService.getServerNodeBy(NumberUtil.intValue(serverId));
            if (node != null) {
                errorLog.setServerId(node.getName());
            }
        }
        errorLog.setMessage(getString(rs, "message"));
        long time = rs.getLong("time");
        errorLog.setTime(rs.wasNull() ? null : time);
        return errorLog;
    };

    private final ClickHouseQueryTemplate clickHouseQueryTemplate;

    public ExceptionLogService(ClickHouseQueryTemplate clickHouseQueryTemplate) {
        this.clickHouseQueryTemplate = clickHouseQueryTemplate;
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

        String sql = "select serverId, message, time"
                + cursorQuerySpec.getWhereSql() + cursorQuerySpec.getOrderBySql() + " limit ?";
        List<ServerErrorLog> content = clickHouseQueryTemplate.query(
                sql,
                ERROR_LOG_ROW_MAPPER,
                queryArgs.toArray());
        boolean overflow = content.size() > pageSize;
        if (overflow) {
            content = new ArrayList<>(content.subList(0, pageSize));
        }
        if (cursorQuerySpec.isReverseResult()) {
            Collections.reverse(content);
        }
//        log.info("exception log cursor sql {} page {}", sql, pageSize);

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

    private QuerySpec buildBaseQuerySpec(ReqQueryItemLog req) {
        StringBuilder whereSql = new StringBuilder(BASE_TABLE_SQL);
        List<Object> args = new ArrayList<>();

        long[] normalizedTimeRange = normalizeTimeRange(req);
        appendTimeRangeCondition(whereSql, args, normalizedTimeRange[0], normalizedTimeRange[1]);
        if (StringUtils.isNotBlank(req.getSid())) {
            whereSql.append(" and serverId = ?");
            args.add(req.getSid().trim());
        }
        appendKeywordCondition(whereSql, args, req.getKeyword());
        return new QuerySpec(whereSql.toString(), args);
    }

    private void appendKeywordCondition(StringBuilder whereSql, List<Object> args, String keyword) {
        if (StringUtils.isBlank(keyword)) {
            return;
        }
        String[] tokens = StringUtils.split(keyword);
        if (tokens == null) {
            return;
        }
        for (String token : tokens) {
            if (token == null || token.isEmpty()) {
                continue;
            }
            if (token.startsWith("~") && token.length() > 1) {
                String excludeWord = token.substring(1);
                whereSql.append(" and message NOT LIKE ?");
                args.add("%" + excludeWord + "%");
            } else {
                whereSql.append(" and message LIKE ?");
                args.add("%" + token + "%");
            }
        }
    }

    private CursorQuerySpec buildCursorQuerySpec(ReqQueryItemLog req, QuerySpec baseQuerySpec) {
        StringBuilder whereSql = new StringBuilder(baseQuerySpec.getWhereSql());
        List<Object> args = new ArrayList<>(baseQuerySpec.getArgs());
        boolean prevCursor = "prev".equalsIgnoreCase(req.getCursorDirection());
        appendCursorCondition(whereSql, args, req, prevCursor);
        String orderBySql = prevCursor
                ? " order by time asc, serverId asc"
                : " order by time desc, serverId desc";
        return new CursorQuerySpec(whereSql.toString(), args, orderBySql, prevCursor);
    }

    private void appendTimeRangeCondition(StringBuilder whereSql, List<Object> args, long startTime, long endTime) {
        whereSql.append(" and time >= ?");
        args.add(startTime);
        whereSql.append(" and time <= ?");
        args.add(endTime);
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
        if (StringUtils.isBlank(req.getCursorSid())) {
            whereSql.append(prevCursor ? " and time > ?" : " and time < ?");
            args.add(req.getCursorTime());
            return;
        }
        if (prevCursor) {
            whereSql.append(" and (time, serverId) > (?, ?)");
        } else {
            whereSql.append(" and (time, serverId) < (?, ?)");
        }
        args.add(req.getCursorTime());
        args.add(req.getCursorSid());
    }

    private static String getString(ResultSet rs, String columnLabel) throws SQLException {
        return rs.getString(columnLabel);
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
