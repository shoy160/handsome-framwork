package cn.handsome.sdk.im.model.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shoy
 * @date 2021/8/6
 */
@NoArgsConstructor
@Data
public class VideoMessage extends BaseMessage {
    @JsonProperty("VideoUrl")
    private String videoUrl;
    @JsonProperty("VideoSize")
    private Integer videoSize;
    @JsonProperty("VideoSecond")
    private Integer videoSecond;
    @JsonProperty("VideoFormat")
    private String videoFormat;
    @JsonProperty("VideoDownloadFlag")
    private Integer videoDownloadFlag;
    @JsonProperty("ThumbUrl")
    private String thumbUrl;
    @JsonProperty("ThumbSize")
    private Integer thumbSize;
    @JsonProperty("ThumbWidth")
    private Integer thumbWidth;
    @JsonProperty("ThumbHeight")
    private Integer thumbHeight;
    @JsonProperty("ThumbFormat")
    private String thumbFormat;
    @JsonProperty("ThumbDownloadFlag")
    private Integer thumbDownloadFlag;
}
