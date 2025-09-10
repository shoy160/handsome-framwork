package cn.handsome.sdk.im.model.callback;

import cn.handsome.sdk.im.model.enums.StateActionEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author shoy
 * @date 2021/10/26
 */
@Getter
@Setter
public class CallbackStateChange extends CallbackBase {
    private Date eventTime;
    private StateChange info;
    private KickedDevice[] kickedDevice;

    @Getter
    @Setter
    public static class StateChange {
        private StateActionEnum action;
        @JsonProperty("To_Account")
        private String account;
        private String reason;
    }

    @Getter
    @Setter
    public static class KickedDevice {
        private String platform;
    }
}
