package cn.handsome.workflow.domain;

import cn.handsome.workflow.enums.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Task Instance
 * @author luoyong
 * @date 2025/7/18
 */
@Getter
@Setter
public class Task {
    private String taskId;
    private String workflowType;
    private String workflowInstanceId;
    private String taskDefName;
    private String workerId;
    private Boolean executed;
    private TaskStatus status;
    private Long scheduledTime;

    /** Time when the task was first polled */
    private Long startTime;

    /** Time when the task completed executing */
    private Long endTime;

    /** Time when the task was last updated */
    private Long updateTime;
    private String reasonForIncompletion;

    private Map<String, Object> inputData;
    private Map<String, Object> outputData;

    private String externalInputPayloadStoragePath;
    private String externalOutputPayloadStoragePath;
    private Integer workflowPriority;
    /**
     * 节点定义
     */
    private WorkflowTask workflowTask;

    public Task() {
        this.inputData = new HashMap<>(0);
        this.outputData = new HashMap<>(0);
    }
}
