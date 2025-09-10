package cn.handsome.thrift.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TServiceClient;

/**
 * @author shoy
 * @date 2021/6/5
 */
@Slf4j
@RequiredArgsConstructor
public class ThriftClient<T extends TServiceClient> implements AutoCloseable {
    private final ThriftClientFactory clientFactory;
    private final T client;

    public T getClient() {
        return this.client;
    }

    @Override
    public void close() {
        log.info("close client: {}", this.client.getClass().getName());
        clientFactory.close(this.client);
    }
}
