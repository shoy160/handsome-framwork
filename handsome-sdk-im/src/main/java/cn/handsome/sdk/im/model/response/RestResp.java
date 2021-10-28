package cn.handsome.sdk.im.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shoy
 * @date 2021/6/18
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestResp {
    @JsonProperty("ActionStatus")
    private String actionStatus;
    @JsonProperty("ErrorInfo")
    private String errorInfo;
    @JsonProperty("ErrorCode")
    private Integer errorCode;
    @JsonProperty("ErrorDisplay")
    private String errorDisplay;

    private final static String STATUS_OK = "OK";

    @JsonIgnore
    public boolean isSuccess() {
        return STATUS_OK.equals(this.actionStatus);
    }

    public RestResp() {
        this.actionStatus = STATUS_OK;
    }
}
