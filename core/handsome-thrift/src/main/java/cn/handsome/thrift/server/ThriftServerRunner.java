package cn.handsome.thrift.server;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;

import java.util.concurrent.*;

/**
 * todo
 *
 * @author shay
 * @date 2021/5/31
 **/
@RequiredArgsConstructor
public class ThriftServerRunner implements CommandLineRunner {

    private final ThriftServer thriftServer;

    @Override
    public void run(String... args) {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNamePrefix("thrift-").build();
        ExecutorService executor = Executors.newFixedThreadPool(1, threadFactory);
        executor.execute(thriftServer::start);
    }
}
