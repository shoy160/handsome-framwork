package cn.handsome.core.test.thread;

import cn.hutool.core.thread.ThreadUtil;
import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author shoy
 * @date 2021/12/24
 */
@AllArgsConstructor
public class SaveThread extends Thread {
    private final Long id;
    private final Map<Long, Boolean> map;

    @Override
    public void run() {
        ThreadUtil.safeSleep(200);
        map.put(this.id, false);
    }
}
