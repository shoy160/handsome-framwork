package cn.handsome.data.wrapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;

/**
 * 支持连表查询的LambdaWrapper
 *
 * @author shay
 * @date 2022/1/1
 **/
public class JoinLambdaQueryChainWrapper<T> extends LambdaQueryChainWrapper<T> {
    public JoinLambdaQueryChainWrapper(BaseMapper<T> baseMapper) {
        super(baseMapper);
    }

    public <S> JoinLambdaQueryChainWrapper<T> selectAs(SFunction<S, ?> column, String alias) {
        // TODO: 2022/1/1
        return this;
    }

    public final JoinLambdaQueryChainWrapper<T> selectAll(Class<?> clazz) {
        return this;
    }

    public <R> JoinLambdaQueryChainWrapper<T> join(String keyWord, boolean condition, Class<R> clazz) {
        if (condition) {
        }
        return this;
    }

}
