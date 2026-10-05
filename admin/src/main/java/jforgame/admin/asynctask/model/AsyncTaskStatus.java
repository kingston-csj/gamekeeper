package jforgame.admin.asynctask.model;

public enum AsyncTaskStatus {

    PENDING,
    PROCESSING,
    SUCCESS,
    FAIL;

    public static AsyncTaskStatus of(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return AsyncTaskStatus.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("不支持的任务状态: " + value);
        }
    }
}
