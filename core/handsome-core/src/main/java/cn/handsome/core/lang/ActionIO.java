package cn.handsome.core.lang;

import java.io.IOException;

/**
 * Action IO异常
 *
 * @author shoy
 * @date 2021/9/23
 */
public interface ActionIO<T> {
    /**
     * 执行操作
     *
     * @param source source
     * @throws IOException ex
     */
    void invoke(T source) throws IOException;
}
