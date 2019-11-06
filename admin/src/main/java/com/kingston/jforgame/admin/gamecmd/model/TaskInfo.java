package com.kingston.jforgame.admin.gamecmd.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qizhao.liao on 2019/11/5.
 * 任务信息
 */
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public Map<Integer, String> getServerStatusMap() {
        return serverStatusMap;
    }

    public void setServerStatusMap(Map<Integer, String> serverStatusMap) {
        this.serverStatusMap = serverStatusMap;
    }

}
