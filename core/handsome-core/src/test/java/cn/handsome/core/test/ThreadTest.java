package cn.handsome.core.test;

import cn.handsome.core.test.thread.SaveThread;
import cn.handsome.core.test.thread.UpdateThread;
import cn.handsome.core.utils.IdentityUtils;
import cn.handsome.core.utils.JsonUtils;
import cn.hutool.core.thread.ThreadUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author shoy
 * @date 2021/12/24
 */
@Slf4j
public class ThreadTest {

    private final Map<Long, Boolean> map = new ConcurrentHashMap<>();

    @Test
    public void saveUpdateTest() throws InterruptedException {
//        List<Thread> threadList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Long id = IdentityUtils.longId();
            SaveThread saveThread = new SaveThread(id, map);
            saveThread.start();
            UpdateThread updateThread = new UpdateThread(id, map);
            updateThread.start();
//            threadList.add(saveThread);
//            threadList.add(updateThread);
        }
        ThreadUtil.safeSleep(2000);
//        for (Thread thread : threadList) {
//            thread.join();
//        }

        log.info(JsonUtils.toJson(map));
    }
}
