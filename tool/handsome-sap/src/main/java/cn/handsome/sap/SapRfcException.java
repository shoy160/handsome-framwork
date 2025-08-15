package cn.handsome.sap;

/**
 *自定义SAP RFC异常类
 * @author luoyong
 * @date 2025/8/12
 */
public class SapRfcException extends RuntimeException {
    public SapRfcException(String message) {
        super(message);
    }

    public SapRfcException(String message, Throwable cause) {
        super(message, cause);
    }
}
