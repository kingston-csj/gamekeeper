package jforgame.admin.gamelog.domain;

import java.util.Map;

/**
 * item_log 表对应实体
 */
import lombok.Data;

@Data
public class ItemLog {

    /**
     * 毫秒时间戳
     */
    private Long time;

    private String module;

    /**
     * 日期字符串
     */
    private String date;

    private String playerId;

    private String sid;

    private String reason;

    /**
     * Map(String,String)
     */
    private Map<String, String> ext;

   
}
