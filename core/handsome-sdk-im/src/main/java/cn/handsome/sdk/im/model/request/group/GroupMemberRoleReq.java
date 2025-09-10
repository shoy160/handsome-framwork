package cn.handsome.sdk.im.model.request.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author shoy
 * @date 2021/6/22
 */
@Data
@NoArgsConstructor
public class GroupMemberRoleReq {
    /**
     * 需要查询的群组 ID
     */
    @JsonProperty("GroupId")
    private String groupId;
    
    /**
     * 表示需要查询的用户帐号(必填)
     * 最多支持500个
     */
    @JsonProperty("User_Account")
    private List<String> userAccount;
}
