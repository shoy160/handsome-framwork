package cn.handsome.core.test;

import cn.handsome.core.logger.LogMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.event.Level;

/**
 *
 * @author luoyong
 * @date 2025/8/1
 */
@Slf4j
public class LoggerTest {
    @Test
    public void logMessageTest() {
        LogMessage message = new LogMessage();
        message.setLevel(Level.DEBUG);
        message.setMessage("test");
        message.put("message", "test02");
        log.info("message: {}", message);
    }
}
