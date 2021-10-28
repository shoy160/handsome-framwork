package cn.handsome.sdk.payment.test;

import cn.handsome.core.domain.dto.ResultDTO;
import cn.handsome.core.utils.JsonUtils;
import cn.handsome.sdk.payment.PaymentClient;
import cn.handsome.sdk.payment.config.PaymentProperties;
import cn.handsome.sdk.payment.entity.PayInputDTO;
import cn.handsome.sdk.payment.entity.TradeDTO;
import cn.handsome.sdk.payment.enums.PaymentMode;
import cn.handsome.sdk.payment.enums.PaymentType;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author shoy
 * @date 2021/6/29
 */
@Slf4j
public class PaymentClientTest {
    private final PaymentClient client;

    public PaymentClientTest() {
        PaymentProperties config = new PaymentProperties();
        config.setGateway("https://pay.handsome.cn/");
//        config.setGateway("http://localhost:25859/");
        config.setProjectCode("test");
        config.setPrivateKey("123456");
        this.client = new PaymentClient(config);
    }

    @Test
    public void createTest() {
        PayInputDTO dto = new PayInputDTO();
        dto.setOrderNo("T100003");
        dto.setAmount(1L);
        dto.setOpenId("okwvj4nhL0Q7TgHLNrZpplL8i7XA");
        dto.setTitle("支付测试");
        dto.setExtend("附加消息");
        dto.setTimeout(2 * 60 * 60);
        ResultDTO<String> payment = client.createPayment(PaymentMode.WeChat, PaymentType.Applet, dto);
        log.info(JsonUtils.toJson(payment));
    }

    @Test
    public void queryTest() {
        ResultDTO<TradeDTO> result = client.query("20210806491174");
        log.info(JsonUtils.toJson(result));
    }
}
