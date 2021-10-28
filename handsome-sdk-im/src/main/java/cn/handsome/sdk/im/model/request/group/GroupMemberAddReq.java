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
public class GroupMemberAddReq {

    /**
     * 要操作的群组（必填）
     */
    @JsonProperty("GroupId")
    private String groupId;
    /**
     * 是否静默加人 (选填)
     */
    @JsonProperty("Silence")
    private Integer silence;
    /**
     * 一次最多添加300个成员
     */
    @JsonProperty("MemberList")
    private List<MemberListDTO> memberList;

    @Data
    @NoArgsConstructor
    public static class MemberListDTO {
        @JsonProperty("Member_Account")
        private String memberAccount;
    }
}
