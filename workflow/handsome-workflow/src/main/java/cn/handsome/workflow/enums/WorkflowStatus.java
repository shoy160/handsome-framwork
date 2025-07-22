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
public enum WorkflowStatus {
    /**
     * 执行中
     */
    RUNNING(false, false),
    COMPLETED(true, true),
    FAILED(true, false),
    TIMED_OUT(true, false),
    TERMINATED(true, false),
    PAUSED(false, true);

    private final boolean terminal;
    private final boolean successful;
}
