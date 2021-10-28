package cn.handsome.demo.web.rest.app;

import cn.handsome.core.domain.dto.ResultDTO;
import cn.handsome.core.gcode.CodeInfo;
import cn.handsome.core.gcode.CodeRule;
import cn.handsome.core.gcode.GlobalCode;
import cn.handsome.web.annotation.EnableAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author shoy
 * @date 2021/7/1
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("app/gcode")
@Api(value = "GlobalCode", tags = "全局编码服务")
public class GlobalCodeRest extends BaseAppRest {

    private final GlobalCode globalCode;

    @PostMapping("gcode/{name}")
    @ApiOperation(value = "注册全局编码", notes = "注册全局编码")
    public ResultDTO<?> registerCode(@PathVariable String name, @RequestBody CodeRule rule) {
        globalCode.register(name, rule);
        return success();
    }

    @PutMapping("gcode/{name}")
    @ApiOperation(value = "编码补仓", notes = "编码补仓")
    public ResultDTO<?> fillCode(@PathVariable String name, int count) {
        globalCode.fill(name, count);
        return success();
    }

    @GetMapping("gcode/{name}")
    @ApiOperation(value = "获取编码", notes = "获取编码")
    public ResultDTO<?> getCode(@PathVariable String name) {
        String code = globalCode.code(name);
        return success(code);
    }

    @GetMapping("gcode/{name}/rule")
    @ApiOperation(value = "获取编码规则", notes = "获取编码规则")
    public ResultDTO<CodeInfo> getRule(@PathVariable String name) {
        CodeInfo info = globalCode.info(name);
        return success(info);
    }

    @EnableAuth(anonymous = true)
    @PostMapping("gcode/{name}/used")
    @ApiOperation(value = "批量使用编码", notes = "批量使用编码")
    public ResultDTO<?> useCodes(@PathVariable String name, @RequestBody String[] codes) {
        globalCode.used(name, codes);
        return success();
    }
}
