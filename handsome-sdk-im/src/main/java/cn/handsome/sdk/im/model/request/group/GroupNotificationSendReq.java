package cn.handsome.sdk.im.model.request.group;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author shoy
 * @date 2021/6/22
 */
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupNotificationSendReq {

    @JsonProperty("GroupId")
    private String groupId;
    @JsonProperty("ToMembers_Account")
    private List<String> toMembersAccount;
    @JsonProperty("Content")
    private String content;
}
