package cn.handsome.sdk.im.model.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author shoy
 * @date 2021/8/6
 */
@NoArgsConstructor
@Data
@Getter
@Setter
public class LocationMessage extends BaseMessage {
    @JsonProperty("Desc")
    private String desc;
    @JsonProperty("Latitude")
    private Double latitude;
    @JsonProperty("Longitude")
    private Double longitude;
}
