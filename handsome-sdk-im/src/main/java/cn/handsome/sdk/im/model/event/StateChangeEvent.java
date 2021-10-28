package cn.handsome.sdk.im.model.event;

import cn.handsome.core.domain.event.BaseEvent;
import cn.handsome.sdk.im.model.enums.PlatformEnum;
import cn.handsome.sdk.im.model.enums.StateActionEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * IM状态变更事件
 *
 * @author shoy
 * @date 2021/10/26
 */
@Getter
@Setter
public class StateChangeEvent extends BaseEvent {
    public final static String ROUTE_KEY = "im_state_change";

    private String userId;
    private StateActionEnum action;
    private Date actionTime;
    private PlatformEnum platform;
    private String clientIp;
    private String reason;
}
