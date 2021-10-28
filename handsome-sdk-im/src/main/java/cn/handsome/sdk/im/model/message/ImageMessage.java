package cn.handsome.sdk.im.model.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author shoy
 * @date 2021/8/6
 */
@NoArgsConstructor
@Data
public class ImageMessage extends BaseMessage {

    @JsonProperty("UUID")
    private String uuid;
    @JsonProperty("ImageFormat")
    private Integer imageFormat;
    @JsonProperty("ImageInfoArray")
    private List<ImageInfoArrayDTO> imageInfoArray;

    @NoArgsConstructor
    @Data
    public static class ImageInfoArrayDTO {
        /**
         * 1.原图;2.大图;3.缩略图
         */
        @JsonProperty("Type")
        private Integer type;
        @JsonProperty("Size")
        private Integer size;
        @JsonProperty("Width")
        private Integer width;
        @JsonProperty("Height")
        private Integer height;
        @JsonProperty("URL")
        private String url;
    }
}
