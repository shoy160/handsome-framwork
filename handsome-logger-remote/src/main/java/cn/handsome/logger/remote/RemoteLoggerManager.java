package cn.handsome.logger.remote;

import ch.qos.logback.core.net.DefaultSocketConnector;
import ch.qos.logback.core.net.SocketConnector;
import cn.handsome.core.logger.LoggerHandler;
import cn.handsome.core.utils.CommonUtils;
import cn.handsome.core.utils.JsonUtils;
import cn.handsome.core.utils.MapUtils;
import cn.handsome.core.utils.TypeUtils;
import cn.handsome.logger.remote.config.RemoteLoggerProperties;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * SocketManager for RemoteLogger
 *
 * @author shay
 * @date 2021/4/7
 */
@Slf4j
public class RemoteLoggerManager {
    private final static String CHARSET = "utf-8";
    private final RemoteLoggerProperties config;
    private final LoggerHandler[] handlers;
    private final BlockingDeque<String> messageQueue;

    public RemoteLoggerManager(RemoteLoggerProperties config, LoggerHandler[] handlers) {
        if (config == null
                || CommonUtils.isEmpty(config.getHost())
                || config.getPort() == 0) {
            this.config = new RemoteLoggerProperties();
            log.info("missing remote logger config");
        } else {
            this.config = config;
        }
        this.handlers = handlers;
        this.messageQueue = new LinkedBlockingDeque<>(this.config.getQueueSize());
        this.start();
    }

    private void start() {
        if (!this.config.isEnable()) {
            return;
        }
        ThreadFactory factory = ThreadFactoryBuilder.create()
                .setNamePrefix("remote-log-")
                .build();
        ScheduledExecutorService swapExpiredPool = new ScheduledThreadPoolExecutor(1, factory);
        swapExpiredPool.scheduleAtFixedRate(this::sendRunner, 0, this.config.getInterval(), TimeUnit.SECONDS);
    }

    private String getMessage(Level level, Object msg) {
        Map<String, Object> map = new HashMap<>();
        if (CommonUtils.isNotEmpty(msg)) {
            if (TypeUtils.isSimple(msg.getClass())) {
                map.put("message", msg);
            } else {
                map = MapUtils.map(msg);
            }
        }
        map.put("level", level);
        if (CommonUtils.isNotEmpty(config.getProject())) {
            map.put("project", config.getProject());
        }
        if (CommonUtils.isNotEmpty(config.getAppName())) {
            map.put("app", config.getAppName());
        }
        if (CommonUtils.isNotEmpty(this.handlers)) {
            for (LoggerHandler handler : this.handlers) {
                try {
                    handler.complete(map);
                } catch (Exception ex) {
                    log.warn("message handler load error:{}", ex.getMessage());
                }
            }
        }
        return JsonUtils.toJson(map);
    }

    public boolean isEnabled(Level level) {
        if (!this.config.isEnable()) {
            return false;
        }
        return level.toInt() >= config.getLevel().toInt();
    }

    private SocketConnector getConnector() throws UnknownHostException {
        InetAddress address = InetAddress.getByName(config.getHost());
        return new DefaultSocketConnector(address, config.getPort(), 0, 30000);
    }

    private void sendData(String message) throws IOException, InterruptedException {
        try (Socket socket = getConnector().call()) {
            try (OutputStream outputStream = socket.getOutputStream()) {
                outputStream.write(message.getBytes(CHARSET));
                outputStream.flush();
            }
        } catch (IOException | InterruptedException ex) {
            boolean inserted = this.messageQueue.offerFirst(message);
            if (!inserted) {
                log.warn("re push queue fail");
            }
            throw ex;
        }
    }

    private void sendRunner() {
        while (true) {
            try {
                String message = this.messageQueue.takeFirst();
                if (CommonUtils.isEmpty(message)) {
                    log.debug("remote logger send complete!");
                    break;
                }
                sendData(message);
            } catch (IOException | InterruptedException ex) {
                log.warn("remote logger sender error:{}", ex.getMessage());
                break;
            }
        }
    }

    public void send(Level level, Object msg) {
        send(level, msg, true);
    }

    public void send(Level level, Object msg, boolean checkLevel) {
        if (!this.config.isEnable()) {
            return;
        }
        if (checkLevel && !isEnabled(level)) {
            if (log.isDebugEnabled()) {
                log.debug(getMessage(level, msg));
            }
            return;
        }
        String message = getMessage(level, msg);
        try {
            this.messageQueue.offer(message, 100, TimeUnit.MICROSECONDS);
        } catch (InterruptedException e) {
            log.warn("there is insufficient space for remote logger queue!");
        }
    }
}
