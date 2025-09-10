package cn.handsome.sdk.im.model.request.group;

import cn.handsome.sdk.im.model.KeyValue;
import cn.handsome.sdk.im.model.enums.GroupRoleEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 群组成员实体
 *
 * @author shoy
 * @date 2021/6/18
 */
@Getter
@Setter
public class GroupMemberReq {
    /**
     * 成员（必填）
     */
    @JsonProperty("Member_Account")
    private String memberAccount;
    /**
     * 赋予该成员的身份,目前备选项只有 Admin（选填)
     */
    @JsonProperty("Role")
    private GroupRoleEnum role;

    /**
     * 群成员维度自定义字段（选填）
     */
    @JsonProperty("AppMemberDefinedData")
    private List<KeyValue> appMemberDefinedData;
}
