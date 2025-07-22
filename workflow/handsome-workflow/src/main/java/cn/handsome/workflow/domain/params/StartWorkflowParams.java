package cn.handsome.workflow.domain.params;

import cn.handsome.workflow.domain.WorkflowDefine;
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
public class StartWorkflowParams {
    private String name;
    private Integer version;
    private String correlationId;
    private Map<String, Object> input;
    private Map<String, String> taskToDomain;
    private WorkflowDefine workflowDef;
    private String externalInputPayloadStoragePath;
    private Integer priority;

    public StartWorkflowParams() {
        this.input = new HashMap<>(0);
        this.taskToDomain = new HashMap<>(0);
        this.priority = 0;
    }

    public StartWorkflowParams(String name, Integer version) {
        this.name = name;
        this.version = version;
    }
}
