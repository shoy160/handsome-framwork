package cn.handsome.sdk.im.model.request.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shoy
 * @date 2021/6/22
 */
@NoArgsConstructor
@Data
public class GroupMemberMsgRecallReq {
    @JsonProperty("GroupId")
    private String groupId;
    @JsonProperty("Sender_Account")
    private String senderAccount;
}
