package cn.handsome.sdk.im.model.callback;

import lombok.Getter;
import lombok.Setter;

/**
 * @author shoy
 * @date 2021/10/26
 */
@Getter
@Setter
public abstract class CallbackBase {
    private String callbackCommand;
}
