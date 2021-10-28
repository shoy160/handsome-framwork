package cn.handsome.sdk.im.model.request.openim;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author shoy
 * @date 2021/6/21
 */
@NoArgsConstructor
@Data
@Getter
@Setter
public class MsgImportReq {
    @JsonProperty("SyncFromOldSystem")
    private Integer syncFromOldSystem;
    @JsonProperty("From_Account")
    private String fromAccount;
    @JsonProperty("To_Account")
    private String toAccount;
    @JsonProperty("MsgRandom")
    private Integer msgRandom;
    @JsonProperty("MsgTimeStamp")
    private Integer msgTimeStamp;
    @JsonProperty("MsgBody")
    private List<MsgBodyDTO> msgBody;
    @JsonProperty("CloudCustomData")
    private String cloudCustomData;
}
