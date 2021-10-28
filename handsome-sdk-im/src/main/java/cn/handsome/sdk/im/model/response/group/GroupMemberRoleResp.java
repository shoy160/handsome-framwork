package cn.handsome.sdk.im.model.response.group;

import cn.handsome.sdk.im.model.enums.GroupRoleEnum;
import cn.handsome.sdk.im.model.response.RestResp;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author shoy
 * @date 2021/6/22
 */
@Getter
@Setter
public class GroupMemberRoleResp extends RestResp {
    @JsonProperty("UserIdList")
    private List<UserIdListDTO> userIdList;

    @NoArgsConstructor
    @Data
    public static class UserIdListDTO {
        @JsonProperty("Member_Account")
        private String memberAccount;
        @JsonProperty("Role")
        private GroupRoleEnum role;
    }
}
