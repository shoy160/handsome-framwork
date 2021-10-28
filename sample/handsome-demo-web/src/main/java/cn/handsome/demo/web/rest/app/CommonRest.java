package cn.handsome.demo.web.rest.app;

import cn.handsome.core.cache.Cache;
import cn.handsome.core.domain.dto.ResultDTO;
import cn.handsome.core.session.Session;
import cn.handsome.demo.client.UserCmd;
import cn.handsome.demo.client.UserRpcService;
import cn.handsome.demo.web.event.TestEvent;
import cn.handsome.demo.web.property.DemoProperties;
import cn.handsome.rabbit.RabbitClient;
import cn.handsome.rocketmq.MessageBuilder;
import cn.handsome.sdk.payment.PaymentClient;
import cn.handsome.sdk.payment.entity.PayInputDTO;
import cn.handsome.sdk.payment.enums.PaymentMode;
import cn.handsome.sdk.payment.enums.PaymentType;
import cn.handsome.thrift.client.ThriftClient;
import cn.handsome.web.annotation.EnableAuth;
import cn.hutool.core.util.RandomUtil;
import com.aliyun.openservices.ons.api.Message;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

/**
 * @author shoy
 * @date 2021/7/1
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("app/common")
@Api(value = "Common", tags = "通用服务")
public class CommonRest extends BaseAppRest {
    private final PaymentClient paymentClient;
    private final Session session;
    private final ThriftClient<UserRpcService.Client> userClient;
    private final RabbitClient rabbitClient;
    private final DemoProperties demoProperties;

    private final Cache<String, Integer> cache;
    private final Cache<String, String> stringCache;
    private final Environment environment;

    @GetMapping("rpc")
    @ApiOperation(value = "RPC测试", notes = "RPC测试")
    public ResultDTO<?> rpcTest() {
        UserRpcService.Client client = userClient.getClient();
        try {
            int result = client.add(new UserCmd());
            return success(result);
        } catch (TException e) {
            e.printStackTrace();
            return fail(e.getMessage());
        }
    }

    @GetMapping("payment")
    @ApiOperation(value = "支付测试", notes = "支付测试")
    public ResultDTO<?> paymentTest() {
        PayInputDTO inputDTO = new PayInputDTO();
        inputDTO.setAmount(1L);
        inputDTO.setOrderNo("MF00001");
        inputDTO.setTitle("订单支付测试");
        ResultDTO<String> result = paymentClient.createPayment(PaymentMode.Alipay, PaymentType.App, inputDTO);
        return result;
    }

    @GetMapping("info")
    @ApiOperation(value = "用户测试", notes = "用户测试")
    public ResultDTO<?> info() {
        session.setClaim("role", "123456");
        return success(session.getClaimAsString("nick"));
    }


    @PostMapping("post")
    @ApiOperation(value = "POST测试", notes = "POST测试")
    public ResultDTO<?> post(@RequestParam(required = true) String name) {
        return success(name);
    }

    @PostMapping("rabbit")
    @EnableAuth(anonymous = true)
    @ApiOperation(value = "RabbitMQ测试", notes = "RabbitMQ测试")
    public ResultDTO<?> rabbit(String msg, int delay) {
        TestEvent event = new TestEvent(msg);
        rabbitClient.send("test_key", event, delay);
        return success();
    }

    @GetMapping("demo_config")
    @EnableAuth(anonymous = true)
    @ApiOperation(value = "Nacos配置测试", notes = "Nacos配置测试")
    public ResultDTO<DemoProperties> demoConfig() {
        String name = environment.resolvePlaceholders("${spring.hikari.pool-name:test}");
        log.info(name);
        return success(demoProperties);
    }

    @GetMapping("cache")
    @EnableAuth(anonymous = true)
    @ApiOperation(value = "Cache测试", notes = "Cache测试")
    public ResultDTO<Integer> cache(String key) {
        int value = RandomUtil.randomInt();
        cache.put(key, value);
        return success(cache.get(key));
    }
}
