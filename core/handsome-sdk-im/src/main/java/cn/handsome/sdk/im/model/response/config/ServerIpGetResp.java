package cn.handsome.sdk.im.model.response.config;

import cn.handsome.sdk.im.model.response.RestResp;
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
public class ServerIpGetResp extends RestResp {
    @JsonProperty("IPList")
    private List<String> ipList;
}
