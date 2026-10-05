package jforgame.admin.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity(name = "t_async_task")
@Data
@NoArgsConstructor
public class AsyncTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_id", unique = true, nullable = false, length = 64)
    private String taskId;

    @Column(name = "task_type", nullable = false, length = 32)
    private String taskType;

    @Column(name = "task_param", columnDefinition = "TEXT")
    private String taskParam;

    /**
     * 操作备注
     */
    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark;

    @Column(name = "status", nullable = false, length = 32)
    private String status;

    @Column(name = "progress", nullable = false)
    private Integer progress;

    @Column(name = "url", length = 1024)
    private String url;

    @Column(name = "message", length = 1024)
    private String message;

    @Column(name = "create_time", nullable = false)
    private Date createTime;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "end_time")
    private Date endTime;
}
