package cn.handsome.workflow.domain;

import cn.handsome.workflow.enums.TaskType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Workflow Define
 * @author luoyong
 * @date 2025/7/18
 */
@Getter
@Setter
public class WorkflowDefine {
    private String name;
    private String description;
    private Integer version;
    private List<WorkflowTask> tasks;
    private Map<String, Object> variables;
    private Map<String, Object> inputTemplate;

    public WorkflowDefine() {
        this.version = 1;
        this.tasks = new ArrayList<>();
        this.variables = new HashMap<>(0);
        this.inputTemplate = new HashMap<>(0);
    }

    public List<WorkflowTask> collectTasks() {
        List<WorkflowTask> tasks = new LinkedList<>();
        for (WorkflowTask workflowTask : this.tasks) {
            tasks.addAll(workflowTask.collectTasks());
        }
        return tasks;
    }

    public void eachTask(Consumer<WorkflowTask> consumer) {
        for (WorkflowTask task : tasks) {
            task.eachTask(consumer);
        }
    }

    public boolean containsType(String taskType) {
        return collectTasks().stream().anyMatch(t -> t.getType().equals(taskType));
    }

    public WorkflowTask getNextTask(String taskReferenceName) {
        WorkflowTask workflowTask = getTaskByRefName(taskReferenceName);
        if (workflowTask != null && TaskType.TERMINATE.name().equals(workflowTask.getType())) {
            return null;
        }

        Iterator<WorkflowTask> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            WorkflowTask task = iterator.next();
            if (task.getTaskReferenceName().equals(taskReferenceName)) {
                // If taskReferenceName matches, break out
                break;
            }
            WorkflowTask nextTask = task.next(taskReferenceName, null);
            if (nextTask != null) {
                return nextTask;
            } else if (TaskType.DO_WHILE.name().equals(task.getType())
                    && !task.getTaskReferenceName().equals(taskReferenceName)
                    && task.has(taskReferenceName)) {
                // If the task is child of Loop Task and at last position, return null.
                return null;
            }

            if (task.has(taskReferenceName)) {
                break;
            }
        }
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }

    public WorkflowTask getTaskByRefName(String taskReferenceName) {
        return collectTasks().stream()
                .filter(
                        workflowTask ->
                                workflowTask.getTaskReferenceName().equals(taskReferenceName))
                .findFirst()
                .orElse(null);
    }
}
