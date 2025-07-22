package cn.handsome.workflow.domain;

import cn.handsome.workflow.enums.WorkflowStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class WorkflowSimple {
    private WorkflowStatus status;
    private String workflowId;
    private String reasonForIncompletion;
    private Set<String> failedReferenceTaskNames;
    private long lastRetriedTime;
    private Set<String> failedTaskNames;

    public WorkflowSimple() {
        this.status = WorkflowStatus.RUNNING;
        this.failedReferenceTaskNames = new HashSet<>();
        this.failedTaskNames = new HashSet<>();
    }
}
