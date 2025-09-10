package cn.handsome.thrift.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.thrift.TProcessor;

/**
 * @author shoy
 * @date 2021/6/4
 */
@Getter
@Setter
public class ThriftDescriptor {
    private String serviceName;
    private TProcessor processor;
    private Class<?> clientClass;
}
