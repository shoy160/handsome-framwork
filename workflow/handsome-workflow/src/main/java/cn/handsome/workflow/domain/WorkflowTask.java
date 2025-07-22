package cn.handsome.workflow.domain;

import cn.handsome.workflow.domain.params.InputParameters;
import cn.handsome.workflow.domain.params.SubWorkflowParams;
import cn.handsome.workflow.enums.TaskType;
import cn.hutool.core.bean.BeanUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Task Define
 * @author luoyong
 * @date 2025/7/18
 */
@Getter
@Setter
public class WorkflowTask {
    private String name;
    private String taskReferenceName;
    private String type;
    private String description;
    private Map<String, Object> inputParameters;
    private Map<String, List<WorkflowTask>> decisionCases;
    private List<WorkflowTask> defaultCase;
    private List<List<WorkflowTask>> forkTasks;
    private List<String> joinOn;

    private SubWorkflowParams subWorkflowParam;
    private TaskDefine taskDefinition;
    /**
     * LoopOver
     */
    private List<WorkflowTask> loopOver;

    public WorkflowTask() {
        this.type = TaskType.SIMPLE.name();
        this.inputParameters = new HashMap<>(0);
        this.decisionCases = new HashMap<>(0);
        this.defaultCase = new LinkedList<>();
        this.forkTasks = new LinkedList<>();
        this.joinOn = new LinkedList<>();
        this.loopOver = new LinkedList<>();
    }

    public boolean isType(TaskType taskType) {
        return Objects.nonNull(taskType) && taskType.name().equals(this.type);
    }

    public InputParameters parameters() {
        return getInputParameter(InputParameters.class);
    }

    public <T> T getInputParameter(Class<T> clazz) {
        return BeanUtil.toBeanIgnoreError(getInputParameters(), clazz);
    }

    private Collection<List<WorkflowTask>> children() {
        Collection<List<WorkflowTask>> workflowTaskLists = new LinkedList<>();
        switch (TaskType.of(type)) {
            case DECISION:
            case SWITCH:
                workflowTaskLists.addAll(decisionCases.values());
                workflowTaskLists.add(defaultCase);
                break;
            case FORK_JOIN:
                workflowTaskLists.addAll(forkTasks);
                break;
            case DO_WHILE:
                workflowTaskLists.add(loopOver);
                break;
            default:
                break;
        }
        return workflowTaskLists;
    }

    public List<WorkflowTask> collectTasks() {
        List<WorkflowTask> tasks = new LinkedList<>();
        tasks.add(this);
        for (List<WorkflowTask> workflowTaskList : children()) {
            for (WorkflowTask workflowTask : workflowTaskList) {
                tasks.addAll(workflowTask.collectTasks());
            }
        }
        return tasks;
    }

    /**
     * 循环处理当前节点任务以及子节点任务
     *
     * @param consumer    节点任务消费者
     */
    public void eachTask(Consumer<WorkflowTask> consumer) {
        List<WorkflowTask> tasks = collectTasks();
        for (WorkflowTask task : tasks) {
            consumer.accept(task);
        }
    }

    public WorkflowTask next(String taskReferenceName, WorkflowTask parent) {
        TaskType taskType = TaskType.of(type);

        switch (taskType) {
            case DO_WHILE:
            case DECISION:
            case SWITCH:
                for (List<WorkflowTask> workflowTasks : children()) {
                    Iterator<WorkflowTask> iterator = workflowTasks.iterator();
                    while (iterator.hasNext()) {
                        WorkflowTask task = iterator.next();
                        if (task.getTaskReferenceName().equals(taskReferenceName)) {
                            break;
                        }
                        WorkflowTask nextTask = task.next(taskReferenceName, this);
                        if (nextTask != null) {
                            return nextTask;
                        }
                        if (task.has(taskReferenceName)) {
                            break;
                        }
                    }
                    if (iterator.hasNext()) {
                        return iterator.next();
                    }
                }
                if (taskType == TaskType.DO_WHILE && this.has(taskReferenceName)) {
                    // come here means this is DO_WHILE task and `taskReferenceName` is the last
                    // task in
                    // this DO_WHILE task, because DO_WHILE task need to be executed to decide
                    // whether to
                    // schedule next iteration, so we just return the DO_WHILE task, and then ignore
                    // generating this task again in deciderService.getNextTask()
                    return this;
                }
                break;
            case FORK_JOIN:
                boolean found = false;
                for (List<WorkflowTask> workflowTasks : children()) {
                    Iterator<WorkflowTask> iterator = workflowTasks.iterator();
                    while (iterator.hasNext()) {
                        WorkflowTask task = iterator.next();
                        if (task.getTaskReferenceName().equals(taskReferenceName)) {
                            found = true;
                            break;
                        }
                        WorkflowTask nextTask = task.next(taskReferenceName, this);
                        if (nextTask != null) {
                            return nextTask;
                        }
                        if (task.has(taskReferenceName)) {
                            break;
                        }
                    }
                    if (iterator.hasNext()) {
                        return iterator.next();
                    }
                    if (found && parent != null) {
                        // we need to return join task... -- get my sibling from my
                        return parent.next(this.taskReferenceName, parent);
                        // parent..
                    }
                }
                break;
            case DYNAMIC:
            case TERMINATE:
            case SIMPLE:
                return null;
            default:
                break;
        }
        return null;
    }

    public boolean has(String taskReferenceName) {
        if (this.getTaskReferenceName().equals(taskReferenceName)) {
            return true;
        }

        switch (TaskType.of(type)) {
            case DECISION:
            case SWITCH:
            case DO_WHILE:
            case FORK_JOIN:
                for (List<WorkflowTask> childx : children()) {
                    for (WorkflowTask child : childx) {
                        if (child.has(taskReferenceName)) {
                            return true;
                        }
                    }
                }
                break;
            default:
                break;
        }
        return false;
    }

    public WorkflowTask get(String taskReferenceName) {
        if (this.getTaskReferenceName().equals(taskReferenceName)) {
            return this;
        }
        for (List<WorkflowTask> tasks : children()) {
            for (WorkflowTask child : tasks) {
                WorkflowTask found = child.get(taskReferenceName);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }
}
