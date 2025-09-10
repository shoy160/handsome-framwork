package cn.handsome.sdk.im.model.request.sns;

import cn.handsome.sdk.im.model.enums.FriendDeleteTypeEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FriendDeleteReq {
    /**
     * 需要删除该 UserID 的好友 (必填)
     */
    @JsonProperty("From_Account")
    private String fromAccount;

    /**
     * 待删除的好友的 UserID 列表 (必填)
     * 单次请求的 To_Account 数不得超过1000
     */
    @JsonProperty("To_Account")
    private List<String> toAccount;

    /**
     * 删除模式(选填)
     */
    @JsonProperty("DeleteType")
    private FriendDeleteTypeEnum deleteType;
}
