package cn.handsome.workflow;

import cn.handsome.workflow.enums.TaskStatus;
import cn.handsome.workflow.enums.WorkflowStatus;

/**
 * 工作流监听
 * @author luoyong
 * @date 2025/7/18
 */
public interface IWorkflowListener {
    /**
     * 状态变更
     * @param workflowId 工作流 ID
     * @param status 工作流状态
     */
    void onStatusChanged(String workflowId, WorkflowStatus status);

    /**
     * 工作节点状态变化
     * @param workflowId 工作流 ID
     * @param taskId 工作节点 ID
     * @param status 工作节点状态
     */
    void onTaskStatusChanged(String workflowId, String taskId, TaskStatus status);
}
