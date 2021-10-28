package cn.handsome.demo.dao;

import cn.handsome.demo.domain.po.UserPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * @author shoy
 * @date 2021/5/28
 */
@Repository
public interface UserMapper extends BaseMapper<UserPO> {
}
