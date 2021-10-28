package cn.handsome.sdk.im.model.response.config;

import cn.handsome.sdk.im.model.response.RestResp;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author shoy
 * @date 2021/6/19
 */
@Getter
@Setter
public class HistoryGetResp extends RestResp {

    @JsonProperty("File")
    private List<FileDTO> file;

    @NoArgsConstructor
    @Data
    public static class FileDTO {
        @JsonProperty("URL")
        private String url;
        @JsonProperty("ExpireTime")
        private String expireTime;
        @JsonProperty("FileSize")
        private Integer fileSize;
        @JsonProperty("FileMD5")
        private String fileMd5;
        @JsonProperty("GzipSize")
        private Integer gzipSize;
        @JsonProperty("GzipMD5")
        private String gzipMd5;
    }
}
