package jforgame.admin.gamelog.io;

import lombok.Data;

import java.util.List;

@Data
public class ReqQueryItemLog {

    private boolean needCount = true;

    private long cachedTotalSize;

    private int cachedTotalPages;

    private int pageNum = 1;

    private int pageSize = 10;

    /**
     * 起始毫秒时间戳，默认 0 表示不限制。
     */
    private long startTime;

    /**
     * 结束毫秒时间戳，默认 0 表示不限制。
     */
    private long endTime;

    private String module;

    private String date;

    private String playerId;

    private String sid;

    /**
     * 关键字过滤，空格分隔。普通词为 AND 包含，~ 前缀为排除。
     * 例如 "a b ~c" 表示必须包含 a 和 b，且不能包含 c。
     */
    private String keyword;

    /**
     * 游标字段，上一页/下一页翻页时使用。
     */
    private long cursorTime;

    private String cursorSid;

    private String cursorPlayerId;

    /**
     * next / prev
     */
    private String cursorDirection;

    /**
     * 统一的筛选条件列表
     */
    private List<FilterItem> filters;

    @Data
    public static class FilterItem {
        /**
         * 字段名称。
         * 如果是固定字段，直接传名称，如 "time", "module", "playerId"
         * 如果是动态字段，加上 ext 前缀，如 "ext.orderId", "ext.status"
         */
        private String field;

        /**
         * 运算符
         * 支持: "=", "!=", ">", "<", ">=", "<=", "between", "in", "like"
         * 如果为空，默认按 "=" 处理
         */
        private String operator;

        /**
         * 查询值 (单值)
         * 用于 "=", "!=", ">", "<", "like"
         */
        private String value;

        /**
         * 查询值 (双值，逗号分隔)
         * 用于 "between" (例如: "2023-01-01,2023-12-31")
         */
        private String rangeValue;

        /**
         * 查询值 (多值，逗号分隔)
         * 用于 "in" (例如: "success,fail")
         */
        private String multiValue;
    }

}
