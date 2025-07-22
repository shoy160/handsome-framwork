package cn.handsome.workflow.domain.params;

import cn.handsome.workflow.domain.WorkflowDefine;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 *
 * @author luoyong
 * @date 2025/7/22
 */
@Getter
@Setter
public class SubWorkflowParams {
    private String name;
    private Integer version;
    private Map<String, String> taskToDomain;

    private WorkflowDefine workflowDefinition;
}
