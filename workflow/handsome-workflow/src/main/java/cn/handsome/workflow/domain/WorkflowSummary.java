package cn.handsome.workflow.domain;

import cn.handsome.core.utils.JsonUtils;
import cn.handsome.workflow.enums.WorkflowStatus;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Getter
@Setter
public class WorkflowSummary {

    /** The time should be stored as GMT */
    private static final TimeZone GMT = TimeZone.getTimeZone("GMT");

    private String workflowType;
    private int version;
    private String workflowId;
    private String correlationId;
    private String startTime;
    private String updateTime;
    private String endTime;
    private WorkflowStatus status;
    private String input;
    private String output;
    private String reasonForIncompletion;
    private long executionTime;
    private String event;
    private String failedReferenceTaskNames = "";
    private String externalInputPayloadStoragePath;
    private String externalOutputPayloadStoragePath;
    private int priority;
    private Set<String> failedTaskNames = new HashSet<>();

    public WorkflowSummary() {
    }

    public WorkflowSummary(Workflow workflow) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(GMT);

        this.workflowType = workflow.getWorkflowName();
        this.version = workflow.getWorkflowVersion();
        this.workflowId = workflow.getWorkflowId();
        this.priority = workflow.getPriority();
        this.correlationId = workflow.getCorrelationId();
        if (workflow.getCreateTime() != null) {
            this.startTime = sdf.format(new Date(workflow.getCreateTime()));
        }
        if (workflow.getEndTime() > 0) {
            this.endTime = sdf.format(new Date(workflow.getEndTime()));
        }
        if (workflow.getUpdateTime() != null) {
            this.updateTime = sdf.format(new Date(workflow.getUpdateTime()));
        }
        this.status = workflow.getStatus();
        if (workflow.getInput() != null) {
            this.input = JsonUtils.toJson(workflow.getInput());
        }
        if (workflow.getOutput() != null) {
            this.output = JsonUtils.toJson(workflow.getOutput());
        }
        this.reasonForIncompletion = workflow.getReasonForIncompletion();
        if (workflow.getEndTime() > 0) {
            this.executionTime = workflow.getEndTime() - workflow.getStartTime();
        }
        this.event = workflow.getEvent();
        this.failedReferenceTaskNames =
                workflow.getFailedReferenceTaskNames().stream().collect(Collectors.joining(","));
        this.failedTaskNames = workflow.getFailedTaskNames();
        if (StrUtil.isNotBlank(workflow.getExternalInputPayloadStoragePath())) {
            this.externalInputPayloadStoragePath = workflow.getExternalInputPayloadStoragePath();
        }
        if (StrUtil.isNotBlank(workflow.getExternalOutputPayloadStoragePath())) {
            this.externalOutputPayloadStoragePath = workflow.getExternalOutputPayloadStoragePath();
        }
    }
}
