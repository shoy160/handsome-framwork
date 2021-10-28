package cn.handsome.sdk.im.model.callback;

import cn.handsome.sdk.im.model.enums.PlatformEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @author shoy
 * @date 2021/10/26
 */
@Getter
@Setter
@ToString
public class CallbackQuery {
    @NotBlank(message = "AppId不能为空")
    private String sdkAppid;
    @NotBlank(message = "回调命令不能为空")
    private String callbackCommand;
    private String contenttype;
    private String clientIP;
    private PlatformEnum optPlatform;
}
