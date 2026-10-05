package jforgame.admin.asynctask.model;

public enum AsyncTaskType {

    SERVER_POWER,
    DB_BACKUP,
    DB_CLEAN,
    
    ;

    public String getValue() {
        return name();
    }


    public static AsyncTaskType of(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("taskType不能为空");
        }
        try {
            return AsyncTaskType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("不支持的任务类型: " + value);
        }
    }
}
