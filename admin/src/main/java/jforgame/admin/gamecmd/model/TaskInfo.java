package jforgame.admin.gamecmd.model;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qizhao.liao on 2019/11/5.
 * 任务信息
 */
@Data
public class TaskInfo {
    private long id;
    private long startTime;
    private long endTime;
    private List<Integer> serverIds;

    /**
     * 每个服的状态(key=serverId)
     */
    private Map<Integer, String > serverStatusMap;

    public TaskInfo(long id, List<Integer> serverIds) {
        this.id = id;
        this.startTime = System.currentTimeMillis();
        this.serverIds = serverIds;
        this.serverStatusMap = new HashMap<>();
    }

}
