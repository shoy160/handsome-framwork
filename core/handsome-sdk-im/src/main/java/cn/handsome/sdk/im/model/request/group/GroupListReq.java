package cn.handsome.sdk.im.model.request.group;

import cn.handsome.sdk.im.model.enums.GroupTypeEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shoy
 * @date 2021/6/18
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupListReq {

    /**
     * 群组形态 (选填)
     */
    @JsonProperty("GroupType")
    private GroupTypeEnum type;

    /**
     * 本次获取的群组 ID 数量的上限 (选填)
     * 不得超过 10000
     */
    @JsonProperty("Limit")
    private Integer limit;

    /**
     * 群太多时分页拉取标志 (选填)
     * 第一次填0,以后填上一次返回的值
     */
    @JsonProperty("Next")
    private Long next;
}
