package cn.handsome.demo.web.rest.app;

import cn.handsome.core.Constants;
import cn.handsome.core.domain.dto.PagedDTO;
import cn.handsome.core.domain.dto.ResultDTO;
import cn.handsome.core.security.Token;
import cn.handsome.core.session.Session;
import cn.handsome.demo.client.UserRpcService;
import cn.handsome.demo.domain.enums.GenderEnum;
import cn.handsome.demo.domain.po.UserPO;
import cn.handsome.demo.service.UserService;
import cn.handsome.demo.web.model.command.LoginCmd;
import cn.handsome.demo.web.model.command.UserCmd;
import cn.handsome.demo.web.model.vo.UserVO;
import cn.handsome.thrift.client.ThriftClient;
import cn.handsome.web.annotation.EnableAuth;
import cn.handsome.web.model.vo.PageVO;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.RandomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
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
@RestController("appUser")
@RequestMapping("app/user")
@Api(value = "User", tags = "用户服务")
public class UserRest extends BaseAppRest {

    private final Snowflake snowflake;
    private final ThriftClient<UserRpcService.Client> userClient;
    private final UserService service;
    private final Session session;

    @PostMapping
    @ApiOperation(value = "添加用户", notes = "添加用户")
    public ResultDTO<?> add(@Valid @RequestBody UserCmd cmd, GenderEnum gender) {
        UserPO entity = toBean(cmd, UserPO.class);
        entity.setId(snowflake.nextId());
        boolean save = service.save(entity);
        log.info(gender.getName());
        return result(save, "添加失败");
    }

    @GetMapping()
    @ApiOperation(value = "用户列表", notes = "用户列表")
    public ResultDTO<List<UserVO>> paged() {
        List<UserPO> list = service.lambdaQuery().orderByDesc(UserPO::getCreateTime).list();
        return success(toListBean(list, UserVO.class));
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

    @GetMapping("detail")
    @ApiOperation(value = "用户详情", notes = "用户详情")
    public ResultDTO<UserVO> detail() {
        UserVO vo = new UserVO();
        vo.setId(session.userId(Long.class, 0L));
        vo.setName(session.getUserName());
        return success(vo);
    }

    @PutMapping("{userId:[0-9]+}")
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

    @DeleteMapping("{userId:[0-9]+}")
    @ApiOperation(value = "删除用户", notes = "删除用户")
    public ResultDTO<?> delete(
            @PathVariable Long userId
    ) {
        UserRpcService.Client client = userClient.getClient();
        try {
            int result = client.add(new cn.handsome.demo.client.UserCmd());
            return success(result);
        } catch (TException e) {
            e.printStackTrace();
        }
        log.info("delete user : {}", userId);
        return success();
    }

    @PostMapping("login")
    @EnableAuth(group = Constants.GROUP_APP, anonymous = true)
    @ApiOperation(value = "登录测试", notes = "登录测试")
    public ResultDTO<?> login(@Valid @RequestBody LoginCmd cmd) {
        Token token = new Token(snowflake.nextId(), cmd.getName());
        token.setClaimValue("nick", "a123456");
        return success(generateToken(token, Constants.GROUP_APP, true));
    }

}
