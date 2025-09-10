package cn.handsome.sdk.im.model.callback;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shoy
 * @date 2021/10/26
 */
@Getter
@Setter
public class CallbackResult {
    @JsonProperty("ActionStatus")
    private String actionStatus;
    @JsonProperty("ErrorInfo")
    private String errorInfo;
    @JsonProperty("ErrorCode")
    private Integer errorCode;

    public CallbackResult() {
        this.actionStatus = "OK";
        this.errorCode = 0;
    }

    public CallbackResult(int code, String errorInfo) {
        this.actionStatus = "FAIL";
        this.errorCode = code;
        this.errorInfo = errorInfo;
    }

    public static CallbackResult success() {
        return new CallbackResult();
    }

    public static CallbackResult error(String msg) {
        return new CallbackResult(500, msg);
    }
}
