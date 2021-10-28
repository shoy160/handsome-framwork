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
public class GroupMemberUnreadNumSetReq {
    @JsonProperty("GroupId")
    private String groupId;
    @JsonProperty("Member_Account")
    private String memberAccount;
    @JsonProperty("UnreadMsgNum")
    private Integer unreadMsgNum;
}
