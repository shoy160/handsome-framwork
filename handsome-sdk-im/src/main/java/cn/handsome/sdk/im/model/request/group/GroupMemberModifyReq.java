package cn.handsome.sdk.im.model.request.group;

import cn.handsome.sdk.im.model.enums.GroupRoleEnum;
import cn.handsome.sdk.im.model.enums.MsgFlagEnum;
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
public class GroupMemberModifyReq {
    @JsonProperty("GroupId")
    private String groupId;
    /**
     * 要操作的群成员 (必填)
     */
    @JsonProperty("Member_Account")
    private String memberAccount;
    /**
     * 成员身份
     * Admin/Member 分别为设置/取消管理员
     */
    @JsonProperty("Role")
    private GroupRoleEnum role;
    /**
     * 消息屏蔽类型 (选填)
     */
    @JsonProperty("MsgFlag")
    private MsgFlagEnum msgFlag;
    /**
     * 群名片（选填）
     */
    @JsonProperty("NameCard")
    private String nameCard;

    /**
     * 指定群成员的禁言时间，单位为秒 (选填)
     */
    @JsonProperty("ShutUpTime")
    private Integer shutUpTime;

    @JsonProperty("AppMemberDefinedData")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<AppMemberDefinedDataDTO> appMemberDefinedData;

    @Data
    @NoArgsConstructor
    public static class AppMemberDefinedDataDTO {
        @JsonProperty("Key")
        private String key;
        @JsonProperty("Value")
        private Object value;
    }
}
