package jforgame.admin.asynctask.model;

import lombok.Data;

@Data
public class ReqSubmitAsyncTask {

    private String taskType;

    /**
     * JSON字符串，按任务类型保存对应参数。
     */
    private String taskParam;


    private String remark;
}
