package cn.handsome.sdk.im.model.request.openim;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author shoy
 * @date 2021/6/21
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MsgBatchSendReq {
    @JsonProperty("SyncOtherMachine")
    private Integer syncOtherMachine;
    @JsonProperty("From_Account")
    private String fromAccount;
    @JsonProperty("To_Account")
    private List<String> toAccount;
    @JsonProperty("MsgRandom")
    private Integer msgRandom;
    @JsonProperty("MsgBody")
    private List<MsgBodyDTO> msgBody;
    @JsonProperty("CloudCustomData")
    private String cloudCustomData;
    @JsonProperty("OfflinePushInfo")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private OfflinePushInfoDTO offlinePushInfo;
}
