package cn.handsome.sdk.im.model.request.openim;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author shoy
 * @date 2021/6/19
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MsgSendReq {
    /**
     * 是否同步消息
     * 1：把消息同步到 From_Account 在线终端和漫游上
     * 2：消息不同步至 From_Account
     */
    @JsonProperty("SyncOtherMachine")
    private Integer syncOtherMachine;
    /**
     * 管理员指定消息发送方帐号
     */
    @JsonProperty("From_Account")
    private String fromAccount;
    @JsonProperty("To_Account")
    private String toAccount;
    @JsonProperty("MsgLifeTime")
    private Integer msgLifeTime;
    @JsonProperty("MsgRandom")
    private Integer msgRandom;
    @JsonProperty("MsgTimeStamp")
    private Integer msgTimeStamp;
    @JsonProperty("ForbidCallbackControl")
    private List<String> forbidCallbackControl;
    @JsonProperty("MsgBody")
    private List<MsgBodyDTO> msgBody;
    @JsonProperty("CloudCustomData")
    private String cloudCustomData;
    @JsonProperty("OfflinePushInfo")
    private OfflinePushInfoDTO offlinePushInfo;
}
