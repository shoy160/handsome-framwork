package cn.handsome.workflow.enums;

/**
 *
 * @author luoyong
 * @date 2025/7/22
 */
public enum TaskRetryLogic {
    /**
     * Fixed
     */
    FIXED,
    EXPONENTIAL_BACKOFF,
    LINEAR_BACKOFF
}
