package jforgame.admin.s3;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProgressMonitor {

    /**
     * 当前进度
     */
    private long progress;

    /**
     * 总大小
     */
    private long maximum;

    /**
     * 开始时间戳
     */
    private long start = System.currentTimeMillis();

}