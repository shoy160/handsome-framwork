package cn.handsome.sdk.im.model.request.group;

import cn.handsome.sdk.im.model.enums.GroupRoleEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 获取群成员详情请求实体
 *
 * @author shoy
 * @date 2021/6/19
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupMemberInfoReq {
    /**
     * 自定义群组 ID (必填)
     */
    @JsonProperty("GroupId")
    private String groupId;

    /**
     * 需要获取哪些信息
     * （Member_Account 被默认包含在其中），如果没有该字段则为群成员全部资料
     */
    @JsonProperty("MemberInfoFilter")
    private List<String> memberInfoFilter;

    /**
     * 群成员身份过滤器
     */
    @JsonProperty("MemberRoleFilter")
    private List<GroupRoleEnum> memberRoleFilter;

    /**
     * 群成员自定义字段过滤器
     */
    @JsonProperty("AppDefinedDataFilter_GroupMember")
    private List<String> appDefinedDataFilterGroupMember;

    /**
     * 一次最多获取多少个成员的资料，不得超过6000。
     * 如果不填，则获取群内全部成员的信息
     */
    @JsonProperty("Limit")
    private Integer limit;

    /**
     * 从第几个成员开始获取
     * 如果不填则默认为0，表示从第一个成员开始获取
     */
    @JsonProperty("Offset")
    private Integer offset;
}
