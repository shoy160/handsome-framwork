package cn.handsome.sdk.im.model.request.group;

import cn.handsome.sdk.im.model.enums.GroupRoleEnum;
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
public class GroupMemberImportReq {

    /**
     * 操作的群 ID (必填)
     */
    @JsonProperty("GroupId")
    private String groupId;
    /**
     * 待添加的群成员数组 (必填)
     */
    @JsonProperty("MemberList")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<MemberListDTO> memberList;

    @NoArgsConstructor
    @Data
    public static class MemberListDTO {
        /**
         * 待导入的群成员帐号
         */
        @JsonProperty("Member_Account")
        private String memberAccount;
        /**
         * 待导入群成员角色
         * 目前只支持填 Admin，不填则为普通成员 Member
         */
        @JsonProperty("Role")
        private GroupRoleEnum role;
        /**
         * 待导入群成员的入群时间
         */
        @JsonProperty("JoinTime")
        private Integer joinTime;
        /**
         * 待导入群成员的未读消息计数
         */
        @JsonProperty("UnreadMsgNum")
        private Integer unreadMsgNum;
    }
}
