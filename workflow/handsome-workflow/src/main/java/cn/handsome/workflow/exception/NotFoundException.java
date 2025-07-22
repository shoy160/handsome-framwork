package cn.handsome.workflow.exception;

/**
 *
 * @author luoyong
 * @date 2025/7/22
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Object... args) {
        super(String.format(message, args));
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
