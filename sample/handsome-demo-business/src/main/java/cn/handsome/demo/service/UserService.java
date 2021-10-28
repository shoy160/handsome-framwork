package cn.handsome.demo.service;

import cn.handsome.demo.domain.po.UserPO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author shoy
 * @date 2021/5/28
 */
public interface UserService extends IService<UserPO> {
    /**
     * 获取用户信息
     *
     * @param userId 用户ID
     * @return 用户名称
     */
    String get(long userId);
}
