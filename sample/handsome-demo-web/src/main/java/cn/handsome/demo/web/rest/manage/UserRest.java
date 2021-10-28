package cn.handsome.demo.web.rest.manage;

import cn.handsome.core.Constants;
import cn.handsome.core.security.Token;
import cn.handsome.web.annotation.EnableAuth;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.RandomUtil;
import cn.handsome.core.domain.dto.PagedDTO;
import cn.handsome.core.domain.dto.ResultDTO;
import cn.handsome.demo.web.model.command.UserCmd;
import cn.handsome.demo.web.model.vo.UserVO;
import cn.handsome.web.model.vo.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shoy
 * @date 2021/5/28
 */
@Slf4j
@RequiredArgsConstructor
@RestController("manageUser")
@RequestMapping("manage/user")
@Api(value = "User", tags = "用户服务")
public class UserRest extends BaseManageRest {

    private final Snowflake snowflake;

    @PostMapping
    @ApiOperation(value = "添加用户", notes = "添加用户")
    public ResultDTO add(@Valid @RequestBody UserCmd cmd) {
        return success();
    }

    @GetMapping()
    @ApiOperation(value = "用户列表", notes = "用户列表")
    public ResultDTO<List<UserVO>> paged() {
        ArrayList<UserVO> list = new ArrayList<>();
        UserVO vo = new UserVO();
        vo.setId(snowflake.nextId());
        vo.setName(RandomUtil.randomString(6));
        list.add(vo);
        return success(list);
    }

    @GetMapping("page")
    @ApiOperation(value = "用户分页列表", notes = "用户分页列表")
    public ResultDTO<PagedDTO<UserVO>> paged(@Valid PageVO page) {
        ArrayList<UserVO> list = new ArrayList<>();
        UserVO vo = new UserVO();
        vo.setId(snowflake.nextId());
        vo.setName(RandomUtil.randomString(6));
        list.add(vo);
        PagedDTO<UserVO> paged = new PagedDTO<>(1, list, page.getPage(), page.getSize());
        return success(paged);
    }

    @GetMapping("{userId}")
    @ApiOperation(value = "用户详情", notes = "用户详情")
    public ResultDTO<UserVO> detail(@PathVariable Long userId) {
        UserVO vo = new UserVO();
        vo.setId(userId);
        vo.setName(RandomUtil.randomString(6));
        return success(vo);
    }

    @PutMapping("{userId}")
    @ApiOperation(value = "编辑用户", notes = "编辑用户")
    public ResultDTO<UserVO> edit(
            @PathVariable Long userId,
            @Valid @RequestBody UserCmd cmd
    ) {
        UserVO vo = new UserVO();
        vo.setId(userId);
        vo.setName(cmd.getName());
        return success(vo);
    }

    @DeleteMapping("{userId}")
    @ApiOperation(value = "删除用户", notes = "删除用户")
    public ResultDTO delete(
            @PathVariable Long userId
    ) {
        log.info("delete user : {}", userId);
        return success();
    }

    @PostMapping("login")
    @EnableAuth(group = Constants.GROUP_MANAGE, anonymous = true)
    @ApiOperation(value = "登录测试", notes = "登录测试")
    public ResultDTO<?> login() {
        Token token = new Token(1001, "shay");
        token.setClaimValue("nick", "a123456");
        return success(generateToken(token, Constants.GROUP_MANAGE));
    }
}
