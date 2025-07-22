package cn.handsome.workflow.domain;

import cn.handsome.workflow.enums.TaskRetryLogic;
import cn.handsome.workflow.enums.TaskTimeoutPolicy;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author luoyong
 * @date 2025/7/22
 */
@Getter
@Setter
public class TaskDefine {
    private String name;
    private String description;
    private int retryCount;
    private long timeoutSeconds;
    private List<String> inputKeys;
    private List<String> outputKeys;
    private TaskTimeoutPolicy timeoutPolicy;
    private TaskRetryLogic retryLogic;
    private int retryDelaySeconds;
    private long responseTimeoutSeconds;
    private Integer concurrentExecLimit;
    private Map<String, Object> inputTemplate;
    private Integer rateLimitPerFrequency;
    private Integer rateLimitFrequencyInSeconds;
    private String isolationGroupId;
    private String executionNameSpace;
    private String ownerEmail;
    private Integer pollTimeoutSeconds;
    private Integer backoffScaleFactor;

    public TaskDefine() {
        this.retryCount = 3;
        this.inputKeys = new ArrayList<>(0);
        this.outputKeys = new ArrayList<>(0);
        this.timeoutPolicy = TaskTimeoutPolicy.TIME_OUT_WF;
        this.retryLogic = TaskRetryLogic.FIXED;
        this.retryDelaySeconds = 60;
        this.responseTimeoutSeconds = 60 * 60;
        this.inputTemplate = new HashMap<>(0);
        this.backoffScaleFactor = 1;
    }

    public TaskDefine(String name) {
        this.name = name;
    }

    public TaskDefine(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public TaskDefine(String name, String description, int retryCount, long timeoutSeconds) {
        this.name = name;
        this.description = description;
        this.retryCount = retryCount;
        this.timeoutSeconds = timeoutSeconds;
    }

    public TaskDefine(
            String name, String description, String ownerEmail,
            int retryCount, long timeoutSeconds, long responseTimeoutSeconds
    ) {
        this.name = name;
        this.description = description;
        this.ownerEmail = ownerEmail;
        this.retryCount = retryCount;
        this.timeoutSeconds = timeoutSeconds;
        this.responseTimeoutSeconds = responseTimeoutSeconds;
    }
}
