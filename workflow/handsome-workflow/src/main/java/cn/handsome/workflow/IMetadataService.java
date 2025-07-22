package cn.handsome.workflow;

import cn.handsome.workflow.domain.params.SubWorkflowParams;
import cn.handsome.workflow.domain.TaskDefine;
import cn.handsome.workflow.domain.WorkflowDefine;
import cn.handsome.workflow.domain.WorkflowTask;
import cn.handsome.workflow.enums.TaskType;
import cn.handsome.workflow.exception.NotFoundException;
import cn.handsome.workflow.exception.TerminateWorkflowException;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 *
 * @author luoyong
 * @date 2025/7/22
 */
public interface IMetadataService {
    Logger logger = LoggerFactory.getLogger(IMetadataService.class);

    /**
     * Create Workflow Define
     * @param def workflow definition
     */
    void createWorkflowDefine(WorkflowDefine def);

    /**
     * Update Workflow Define
     * @param def workflow definition
     */
    void updateWorkflowDefine(WorkflowDefine def);

    /**
     * @param name Name of the workflow definition to be removed
     * @param version Version of the workflow definition to be removed
     */
    void removeWorkflowDefine(String name, Integer version);

    Optional<WorkflowDefine> getLatestWorkflowDefine(String name);

    Optional<WorkflowDefine> getWorkflowDefine(String name, int version);

    /**
     * @param taskDef task definition to be created
     */
    TaskDefine createTaskDefine(TaskDefine taskDef);

    /**
     * @param taskDef task definition to be updated.
     * @return name of the task definition
     */
    TaskDefine updateTaskDefine(TaskDefine taskDef);

    /**
     * @param name Name of the task
     * @return Task Definition
     */
    TaskDefine getTaskDefine(String name);

    /**
     * @return All the task definitions
     */
    List<TaskDefine> getAllTaskDefines();

    /**
     * @param name Name of the task
     */
    void removeTaskDefine(String name);

    default WorkflowDefine lookupForWorkflowDefine(String name, Integer version) {
        Optional<WorkflowDefine> defineOptional = Objects.isNull(version)
                ? getLatestWorkflowDefine(name)
                : getWorkflowDefine(name, version);
        return defineOptional.orElseThrow(
                () -> {
                    logger.error(
                            "There is no workflow defined with name {} and version {}",
                            name, version);
                    return new NotFoundException(
                            "No such workflow defined. name=%s, version=%s", name, version);
                });
    }

    default void populateTaskDefine(WorkflowDefine workflowDefine) {
        workflowDefine.eachTask(this::populateTaskDefine);
    }

    default void populateTaskDefine(WorkflowTask workflowTask) {
        if (Objects.isNull(workflowTask.getTaskDefinition())) {
            TaskDefine taskDefine = getTaskDefine(workflowTask.getName());
            if (Objects.isNull(taskDefine) && workflowTask.isType(TaskType.SIMPLE)) {
                // ad-hoc task def
                taskDefine = new TaskDefine(workflowTask.getName());
            }
            workflowTask.setTaskDefinition(taskDefine);
        }
        // SUB_WORKFLOW
        if (workflowTask.getType().equals(TaskType.SUB_WORKFLOW.name())) {
            populateVersionForSubWorkflow(workflowTask);
        }
    }

    default void populateVersionForSubWorkflow(WorkflowTask workflowTask) {
        SubWorkflowParams subworkflowParams = workflowTask.getSubWorkflowParam();
        if (Objects.isNull(subworkflowParams)) {
            return;
        }
        if (Objects.isNull(subworkflowParams.getVersion())) {
            String subWorkflowName = subworkflowParams.getName();
            Integer subWorkflowVersion =
                    getLatestWorkflowDefine(subWorkflowName)
                            .map(WorkflowDefine::getVersion)
                            .orElseThrow(
                                    () -> {
                                        String reason =
                                                String.format(
                                                        "The Task %s defined as a sub-workflow has no workflow definition available ",
                                                        subWorkflowName);
                                        logger.error(reason);
                                        return new TerminateWorkflowException(reason);
                                    });
            subworkflowParams.setVersion(subWorkflowVersion);
        }
    }
}
