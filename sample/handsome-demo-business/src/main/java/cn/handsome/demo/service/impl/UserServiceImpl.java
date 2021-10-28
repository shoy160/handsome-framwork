package cn.handsome.demo.service.impl;

import cn.handsome.demo.dao.UserMapper;
import cn.handsome.demo.domain.po.UserPO;
import cn.handsome.demo.service.UserService;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author shoy
 * @date 2021/5/28
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserPO> implements UserService {
    @Override
    public String get(long userId) {
        log.info("random");
        return RandomUtil.randomString(12);
//        UserPO entity = lambdaQuery()
//                .eq(UserPO::getId, userId)
//                .one();
//        if (entity == null) {
//            return null;
//        }
//        return entity.getName();
    }
}
