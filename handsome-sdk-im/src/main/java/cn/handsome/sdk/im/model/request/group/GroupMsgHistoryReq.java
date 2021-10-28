package cn.handsome.sdk.im.model.request.group;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shoy
 * @date 2021/6/22
 */
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupMsgHistoryReq {
    @JsonProperty("GroupId")
    private String groupId;
    @JsonProperty("ReqMsgSeq")
    private Integer reqMsgSeq;
    /**
     * 拉取消息的最大 seq (选填)
     */
    @JsonProperty("ReqMsgNumber")
    private Integer reqMsgNumber;
}
