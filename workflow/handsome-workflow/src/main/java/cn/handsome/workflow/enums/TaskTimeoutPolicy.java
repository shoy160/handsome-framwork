package cn.handsome.workflow.enums;

/**
 *
 * @author luoyong
 * @date 2025/7/22
 */
public enum TaskTimeoutPolicy {
    /**
     * Retry
     */
    RETRY,
    TIME_OUT_WF,
    ALERT_ONLY
}
