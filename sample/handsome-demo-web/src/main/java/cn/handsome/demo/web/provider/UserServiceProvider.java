package cn.handsome.demo.web.provider;

import cn.handsome.demo.client.UserCmd;
import cn.handsome.demo.client.UserRpcService;
import cn.handsome.thrift.annotation.ThriftService;
import org.apache.thrift.TException;

/**
 * 用户服务提供者
 *
 * @author shoy
 * @date 2021/6/4
 */
@ThriftService
public class UserServiceProvider implements UserRpcService.Iface {

    @Override
    public int add(UserCmd cmd) throws TException {
        return 52000;
    }
}
