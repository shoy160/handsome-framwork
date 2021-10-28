package cn.handsome.demo.web.event;

import cn.handsome.core.domain.event.BaseEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author shoy
 * @date 2021/8/23
 */
@Getter
@Setter
@NoArgsConstructor
public class TestEvent extends BaseEvent {
    private String message;

    public TestEvent(String message) {
        super();
        this.message = message;
    }
}
