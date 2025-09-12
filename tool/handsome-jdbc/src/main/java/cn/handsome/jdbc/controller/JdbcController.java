package cn.handsome.jdbc.controller;

import cn.handsome.jdbc.manage.JdbcBuilder;
import cn.handsome.jdbc.model.JdbcQueryCmd;
import cn.handsome.web.base.BaseController;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author luoyong
 * @date 2025/8/20
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/jdbc")
@Api(value = "jdbc", tags = "JDBC 接口")
public class JdbcController extends BaseController {
    @PostMapping("query")
    public Object query(@RequestBody JdbcQueryCmd cmd) {
        return JdbcBuilder.queryData(cmd);
    }

    @PostMapping("execute")
    public Object execute(@RequestBody JdbcQueryCmd cmd) {
        return JdbcBuilder.execute(cmd);
    }
}
