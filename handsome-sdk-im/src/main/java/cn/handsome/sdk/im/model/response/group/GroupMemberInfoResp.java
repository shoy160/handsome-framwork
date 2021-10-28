package cn.handsome.sdk.im.model.response.group;

import cn.handsome.sdk.im.model.response.RestResp;
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
public class GroupMemberInfoResp extends RestResp {
    /**
     * 本群组的群成员总数
     */
    @JsonProperty("MemberNum")
    private Integer memberNum;

    /**
     * 群成员列表
     */
    @JsonProperty("MemberList")
    private List<GroupMemberResp> memberList;
}
