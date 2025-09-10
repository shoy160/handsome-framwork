package cn.handsome.data.handler;

import cn.handsome.core.session.Session;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author shay
 * @date 2020/11/11
 */
@Component
@RequiredArgsConstructor
public class MybatisMetaObjectHandler implements MetaObjectHandler {
    private final Session session;

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
        this.strictInsertFill(metaObject, "creatorId", Object.class, session.getUserId());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
    }
}
