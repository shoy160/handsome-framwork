package cn.handsome.workflow.exception;

import cn.handsome.workflow.domain.Task;
import cn.handsome.workflow.enums.WorkflowStatus;
import lombok.Getter;

/**
 *
 * @author luoyong
 * @date 2025/7/22
 */
@Getter
public class TerminateWorkflowException extends RuntimeException {

    private final WorkflowStatus workflowStatus;
    private final Task task;

    public TerminateWorkflowException(String reason) {
        this(reason, WorkflowStatus.FAILED);
    }

    public TerminateWorkflowException(String reason, WorkflowStatus workflowStatus) {
        this(reason, workflowStatus, null);
    }

    public TerminateWorkflowException(
            String reason, WorkflowStatus workflowStatus, Task task) {
        super(reason);
        this.workflowStatus = workflowStatus;
        this.task = task;
    }
}
