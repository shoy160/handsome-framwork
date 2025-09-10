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
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupMemberDeleteReq {

    @JsonProperty("GroupId")
    private String groupId;
    /**
     * 是否静默删除（选填）
     */
    @JsonProperty("Silence")
    private Integer silence;

    /**
     * 要删除的群成员列表，最多500个
     */
    @JsonProperty("MemberToDel_Account")
    private List<String> memberToDelAccount;

    /**
     * 踢出用户原因（选填）
     */
    @JsonProperty("Reason")
    private String reason;
}
