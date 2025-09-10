package cn.handsome.sdk.im.model.request.config;

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
public class PortraitGetReq {
    @JsonProperty("To_Account")
    private List<String> toAccount;
    @JsonProperty("TagList")
    private List<String> tagList;
}
