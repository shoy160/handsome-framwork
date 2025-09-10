package cn.handsome.sdk.im.model.request.account;

import cn.handsome.sdk.im.model.BaseUserId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author shoy
 * @date 2021/6/18
 */
@Getter
@Setter
public class AccountsDeleteReq {
    @JsonProperty("DeleteItem")
    private List<BaseUserId> items;
}
