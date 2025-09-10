package cn.handsome.core.domain.event;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * 基础事件类
 *
 * @author shay
 * @date 2020/9/29
 */
@Setter
@Getter
public abstract class BaseEvent implements Serializable {
    private static final long serialVersionUID = 8211486444279235790L;

    private String eventId;
    private Date eventTime;

    protected BaseEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.eventTime = new Date();
    }
}
