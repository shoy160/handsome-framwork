package cn.handsome.workflow.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author luoyong
 * @date 2025/7/21
 */
@Getter
@Setter
public class RunWorkflow {
    private WorkflowDefine define;
    private Map<String, Object> input;
    private String inputStoragePath;
    private Integer priority;
    private String workflowId;
    private String correlationId;
    private String parentWorkflowId;
    private String parentWorkflowTaskId;
    private String reRunFromWorkflowId;

    public RunWorkflow(WorkflowDefine define) {
        this(define, new HashMap<>(0));
    }

    public RunWorkflow(WorkflowDefine define, Map<String, Object> input) {
        this.define = define;
        this.input = input;
    }
}
