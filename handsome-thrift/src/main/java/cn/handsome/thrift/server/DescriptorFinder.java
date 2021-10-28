package cn.handsome.thrift.server;

import cn.handsome.thrift.domain.ThriftDescriptor;

/**
 * Processor 查找器
 *
 * @author shoy
 * @date 2021/6/4
 */
public interface DescriptorFinder {
    /**
     * 查找Processor
     *
     * @return TProcessor[]
     */
    ThriftDescriptor[] find();
}
