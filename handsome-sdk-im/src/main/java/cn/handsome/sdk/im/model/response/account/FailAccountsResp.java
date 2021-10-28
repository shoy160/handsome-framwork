package cn.handsome.sdk.im.model.response.account;

import cn.handsome.sdk.im.model.response.RestResp;
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
public class FailAccountsResp extends RestResp {
    @JsonProperty("FailAccounts")
    public List<String> fails;
}
