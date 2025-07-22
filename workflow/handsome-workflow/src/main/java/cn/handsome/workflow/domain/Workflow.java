package cn.handsome.workflow.domain;

import cn.handsome.workflow.enums.WorkflowStatus;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Workflow Instance
 * @author luoyong
 * @date 2025/7/18
 */
@Getter
@Setter
public class Workflow extends Auditable {
    private String workflowId;
    private WorkflowStatus status;
    private List<Task> tasks;
    private WorkflowDefine workflowDefinition;
    private String parentWorkflowId;
    private String parentWorkflowTaskId;
    private Map<String, Object> variables;
    private Map<String, Object> input;
    private Map<String, Object> output;
    private String externalInputPayloadStoragePath;
    private String externalOutputPayloadStoragePath;
    private Long endTime;
    private Integer priority;
    private String correlationId;
    private String reRunFromWorkflowId;
    private String reasonForIncompletion;
    private String event;
    private Map<String, String> taskToDomain;
    private Set<String> failedReferenceTaskNames;
    private Set<String> failedTaskNames;

    public Workflow() {
        this.status = WorkflowStatus.RUNNING;
        this.tasks = new ArrayList<>(0);
        this.input = new HashMap<>(0);
        this.output = new HashMap<>(0);
        this.taskToDomain = new HashMap<>(0);
        this.failedReferenceTaskNames = new HashSet<>(0);
        this.failedTaskNames = new HashSet<>(0);
        this.variables = new HashMap<>(0);
    }

    public Workflow(RunWorkflow runWorkflow) {
        this();
        this.workflowId = StrUtil.isBlank(runWorkflow.getWorkflowId())
                ? IdUtil.fastUUID()
                : runWorkflow.getWorkflowId();
        if (StrUtil.isNotBlank(runWorkflow.getCorrelationId())) {
            this.correlationId = runWorkflow.getCorrelationId();
        }
        if (MapUtil.isNotEmpty(runWorkflow.getInput())) {
            this.input = runWorkflow.getInput();
        }
        WorkflowDefine define = runWorkflow.getDefine();
        this.workflowDefinition = define;
        this.priority = runWorkflow.getPriority();
        this.variables = define.getVariables();
        if (StrUtil.isNotBlank(runWorkflow.getInputStoragePath())) {
            this.externalInputPayloadStoragePath = runWorkflow.getInputStoragePath();
        }
    }

    public Long getStartTime() {
        return this.getCreateTime();
    }

    public String getWorkflowName() {
        if (workflowDefinition == null) {
            throw new NullPointerException("Workflow definition is null");
        }
        return workflowDefinition.getName();
    }

    public int getWorkflowVersion() {
        if (workflowDefinition == null) {
            throw new NullPointerException("Workflow definition is null");
        }
        return workflowDefinition.getVersion();
    }
}
