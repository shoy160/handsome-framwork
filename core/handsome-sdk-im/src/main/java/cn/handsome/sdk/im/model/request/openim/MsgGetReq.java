package cn.handsome.sdk.im.model.request.openim;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shoy
 * @date 2021/6/21
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MsgGetReq {
    @JsonProperty("From_Account")
    private String fromAccount;
    @JsonProperty("To_Account")
    private String toAccount;
    @JsonProperty("MaxCnt")
    private Integer maxCnt;
    @JsonProperty("MinTime")
    private Integer minTime;
    @JsonProperty("MaxTime")
    private Integer maxTime;
    @JsonProperty("LastMsgKey")
    private String lastMsgKey;
}
