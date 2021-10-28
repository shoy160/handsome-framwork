package cn.handsome.sdk.im.model.response.group;

import cn.handsome.sdk.im.model.response.RestResp;
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
public class GroupMemberAddResp extends RestResp {
    @JsonProperty("MemberList")
    private List<MemberListDTO> memberList;

    @NoArgsConstructor
    @Data
    public static class MemberListDTO {
        @JsonProperty("Member_Account")
        private String memberAccount;
        /**
         * 加人结果
         * 0 为失败；1 为成功；2 为已经是群成员
         */
        @JsonProperty("Result")
        private Integer result;
    }
}
