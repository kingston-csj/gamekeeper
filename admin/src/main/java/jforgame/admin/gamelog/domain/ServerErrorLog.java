package jforgame.admin.gamelog.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ServerErrorLog {

    private String serverId;
    private String message;
    /**
     * 毫秒时间戳
     */
    private Long time;
}
