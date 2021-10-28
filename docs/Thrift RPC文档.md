### Thrift RPC

```xml
<!-- pom.xml -->
<dependencies>
    <dependency>
        <groupId>cn.handsome.framework</groupId>
        <artifactId>handsome-thrift</artifactId>
    </dependency>
    <dependency>
        <groupId>cn.handsome.service</groupId>
        <artifactId>handsome-client</artifactId>
        <version>1.0.1</version>
    </dependency>
</dependencies>
```
#### 服务提供者

```yaml
# application.yml
handsome:
  thrift:
    server:
      port: 8090
#      service: 127.0.0.1
#      service-port: 8090
```



```java
/** 开启Thrift服务 */
@EnableThriftServer
@SpringBootApplication
@ComponentScan(Constants.BASE_PACKAGES)
public class DemoApplication {
	public static void main(String[] args) {
        HandsomeApplication.run(DemoConstants.SERVICE_NAME, DemoApplication.class, args);
    }
}

/** 接口实现 */
@ThriftService
public class XxxRpcServiceImpl implements XxxRpcService.Iface {
    
}
```




#### 消费者
```java
/** 消费者 */
@RequiredArgsConstructor
public class XXXServiceImpl implements XXXService {
    private final ThriftClientFactory clientFactory;
    
    public void test(){
        //创建RPC客户端
        XxxRpcService.Client client = clientFactory.create(XxxRpcService.Client.class);
        
        //调用RPC方法
        client.xxx();
        
        //关闭client
        clientFactory.close(client);
    }
}
```