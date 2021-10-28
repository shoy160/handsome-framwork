### 包引用

```xml
<dependency>
    <groupId>cn.handsome.framework</groupId>
    <artifactId>handsome-payment-sdk</artifactId>
</dependency>
```



### 配置信息

```yaml
handsome:
  payment:
    gateway: https://pay.handsome.cn
    project-code: xxx
    private-key: xxx
```



### 调用方法

```java
/** 发起支付 */
@RequiredArgsConstructor
public class XXXServiceImpl implements XXXService {
    private final PaymentClient paymentClient;
    
    public void test(){
        PayInputDTO inputDTO = new PayInputDTO();
        inputDTO.setAmount(1L);
        inputDTO.setOrderNo("MF00001");
        inputDTO.setTitle("订单支付测试");
        ResultDTO<String> result = paymentClient.createPayment(PaymentMode.Alipay, PaymentType.App, inputDTO);
    }
}

/** 支付回调 */
@RestController
@RequestMapping("notify")
@RequiredArgsConstructor
public class NotifyRest {
    private final static String TAG_SUCCESS = "success";
    private final static String TAG_FAIL = "fail";
    private final PaymentClient paymentClient;
    
    @ApiIgnore
    @PostMapping("pay")
    public String pay(@RequestBody NotifyDTO dto){
        boolean verify = paymentClient.verify(dto);
        if(verify){
            //do something..
            return TAG_SUCCESS;
        }
        return TAG_FAIL;
    }
}

```

