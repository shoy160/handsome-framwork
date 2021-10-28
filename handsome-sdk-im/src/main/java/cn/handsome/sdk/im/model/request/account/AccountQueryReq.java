package cn.handsome.sdk.im.model.request.account;

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
public class AccountQueryReq {
    @JsonProperty("To_Account")
    private List<String> accounts;
    @JsonProperty("IsNeedDetail")
    private int needDetail;
}
