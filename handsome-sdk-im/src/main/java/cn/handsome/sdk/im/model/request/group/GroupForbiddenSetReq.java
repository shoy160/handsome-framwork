package cn.handsome.sdk.im.model.request.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 禁言设置
 *
 * @author shoy
 * @date 2021/6/22
 */
@NoArgsConstructor
@Data
public class GroupForbiddenSetReq {
    /**
     * 需要查询的群组 ID
     */
    @JsonProperty("GroupId")
    private String groupId;
    /**
     * 需要禁言的用户帐号
     * 最多支持500个帐号
     */
    @JsonProperty("Members_Account")
    private List<String> membersAccount;
    /**
     * 需禁言时间，单位为秒
     * 为0时表示取消禁言
     */
    @JsonProperty("ShutUpTime")
    private Integer shutUpTime;
}
