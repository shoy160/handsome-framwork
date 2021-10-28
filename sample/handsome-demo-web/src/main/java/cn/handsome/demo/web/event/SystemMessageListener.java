package cn.handsome.demo.web.event;

import cn.handsome.rocketmq.MessageBuilder;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shoy
 * @date 2021/6/10
 */
@Slf4j
//@MqListener(topic = "sysmsg")
public class SystemMessageListener implements MessageListener {
    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {
        System.out.println("Receive: " + message);
        String event = new MessageBuilder(message).getEvent(String.class);
        log.info(event);
        try {
            //do something..
            return Action.CommitMessage;
        } catch (Exception e) {
            //消费失败
            return Action.ReconsumeLater;
        }
    }
}
