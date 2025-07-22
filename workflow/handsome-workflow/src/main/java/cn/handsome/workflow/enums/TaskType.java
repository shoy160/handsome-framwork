package cn.handsome.workflow.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author luoyong
 * @date 2025/7/18
 */
@Getter
@RequiredArgsConstructor
public enum TaskType {
    /**
     * SIMPLE
     */
    SIMPLE(false),
    DYNAMIC(false),
    FORK(true),
    FORK_JOIN(false),
    FORK_JOIN_DYNAMIC(false),
    DECISION(true),
    SWITCH(true),
    JOIN(true),
    DO_WHILE(true),
    SUB_WORKFLOW(false),
    START_WORKFLOW(false),
    EVENT(false),
    WAIT(false),
    HUMAN(false),
    USER_DEFINED(false),
    HTTP(false),
    HTTP_WAIT(false),
    LAMBDA(false),
    INLINE(false),
    EXCLUSIVE_JOIN(true),
    TERMINATE(false),
    KAFKA_PUBLISH(false),
    JSON_JQ_TRANSFORM(false),
    SET_VARIABLE(false);
    private final boolean builtIn;

    public static TaskType of(String taskType) {
        try {
            return TaskType.valueOf(taskType);
        } catch (IllegalArgumentException iae) {
            return TaskType.USER_DEFINED;
        }
    }
}
