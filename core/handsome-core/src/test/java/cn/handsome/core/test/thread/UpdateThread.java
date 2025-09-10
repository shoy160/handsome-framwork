package cn.handsome.core.test.thread;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author shoy
 * @date 2021/12/24
 */
@AllArgsConstructor
public class UpdateThread extends Thread {
    private final Long id;
    private final Map<Long, Boolean> map;

    @SneakyThrows
    @Override
    public void run() {
        if (map.containsKey(id)) {
            map.put(id, true);
        }
    }
}
