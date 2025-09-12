package cn.handsome.demo.service.impl;

import cn.handsome.demo.entity.TRecord;
import cn.handsome.demo.mapper.TRecordMapper;
import cn.handsome.demo.service.ITRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 认证数据表 服务实现类
 * </p>
 *
 * @author shoy
 * @since 2022-01-13
 */
@Service
public class TRecordServiceImpl extends ServiceImpl<TRecordMapper, TRecord> implements ITRecordService {

}
