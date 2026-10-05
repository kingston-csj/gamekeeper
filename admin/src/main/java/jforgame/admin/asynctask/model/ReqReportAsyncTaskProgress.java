package jforgame.admin.asynctask.model;

import lombok.Data;

@Data
public class ReqReportAsyncTaskProgress {

    private Integer progress;

    /**
     * 上报的额外信息，例如进度完成后的cdn链接
     */
    private String data;
}
