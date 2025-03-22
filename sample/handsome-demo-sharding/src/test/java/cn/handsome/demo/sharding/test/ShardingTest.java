package cn.handsome.demo.sharding.test;

import cn.handsome.core.utils.JsonUtils;
import cn.handsome.demo.sharding.ShardingApplication;
import cn.handsome.demo.sharding.dao.RecordMapper;
import cn.handsome.demo.sharding.domain.po.RecordPO;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Random;

/**
 * todo
 *
 * @author shay
 * @date 2022/8/6
 **/
@Slf4j
@SpringBootTest(classes = {ShardingApplication.class})
public class ShardingTest {
    @Resource
    private DataSource dataSource;

    private RecordMapper mapper;

    @Autowired
    public void setMapper(RecordMapper mapper) {
        this.mapper = mapper;
    }

    @Test
    @SneakyThrows
    void insertTest() {
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        Long eventId = RandomUtil.randomLong();
        statement.execute("insert into record values ('5212636','123456',123,542143,'124567','','127.0.0.1','useragent')");

    }

    @Test
    void selectTest() {
        List<RecordPO> list = ChainWrappers.lambdaQueryChain(mapper)
                .list();
        log.info(JsonUtils.toJson(list));
    }
}
