package cn.handsome.workflow.domain.params;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author luoyong
 * @date 2025/7/22
 */
@Getter
@Setter
public class RerunWorkflowParams {
    private String reRunFromWorkflowId;
    private Map<String, Object> workflowInput;
    private String reRunFromTaskId;
    private Map<String, Object> taskInput;
    private String correlationId;

    public RerunWorkflowParams() {
        this.workflowInput = new HashMap<>(0);
        this.taskInput = new HashMap<>(0);
    }
}
