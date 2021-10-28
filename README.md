### 更新日志

```
v1.1.4
1. fixed Redis缓存泛型注入异常,导入handsome-cache将自动注入Cache,无需重复注入;
2. IM SDK新增回调基类定义。

v1.1.3
1. 新增对账号单点登录的支持;
2. 新增对全局账号禁用的支持;
3. 添加handsome-cache的默认缓存配置,取消默认过期时间，新增随机过期时间的支持;
4. 统一封装Token生成以及Token校验接口，并提供默认实现;
5. 优化Token存储方式以及认证拦截器;
6. 分离Token对象和JwtTokenBuilder。

v1.1.2
1. 增强map映射，支持自定义keyEditor;
2. 修复RabbitMQ，通过Binding注册时交换机、队列未定义；
3. 其他部分辅件优化。

v1.1.1
1. 新增源码组件，支持源码注释查看;
2. 修复sdk-im中签名缓存失效的问题;
3. 进一步封装IM客户端SDK,修复ImId生成规则问题。


v1.0.21
1. 支付SDK新增交易查询；
2. 新增handsome-rabbit模块，支持RabbitMQ相关操作；(docs/MQ文档)
3. 统一模块名称handsome-im-sdk -> handsome-sdk-im,handsome-payment-sdk -> handsome-sdk-payment;
4. 其他相关Bug修复。

v1.0.20
1. 优化IM多环境封装；
2. 修复IM离线消息设置。

v1.0.18
1. 封装IM常用方法，并处理ID生成规则以及群组字符截断。

v1.0.17
1. 修复雪花ID在分布式系统中，偶尔出现生成重复ID的问题；
2. 封装IM常用接口的简化版本辅助类SimplifyImClient；
3. 修复部分其他Bug以及结构优化。

v1.0.16
1. 修复PostMapping读取Query参数异常；
2. 修复参数异常提示。

v1.0.15
1. 修复JWT验证导致request body读取异常的问题；
2. 优化跨域支持；
3. 添加Html辅助类;
4. 修复部分已知bug。

v1.0.13
1. 更新IM SDK离线消息实体；
2. 修复远程日志默认服务地址,以及RemoteLogger日志等级支持；
3. 修复Nacos服务注册，健康状态问题。

v1.0.12
1. 新增全局编码接口GlobalCode，支持各种类型的编码生成规则；
2. 新增RedisLock分布式锁的支持，cache模块引入Redission；
3 新增基于MybatisPlus的常用TypeHandler,用户PO实体转换，如Json字符、分隔符转换等；
4. 优化枚举类型支持，支持Value自动转换；
5. 支付SDK新增支付超时时间配置；
6. handsome-data新增MybatisPlus默认分页配置，@MapperScan需移至Application;
7. 修复JwtToken默认过期时间问题。

v1.0.11
1. MQ有序队列订阅组分离;
2. 统一IM SDK包名;
3. 解决部分已知的bug。

v1.0.10
1. 引入Nacos模块，添加默认Nacos服务配置；
2. Thrift模块优先使用Nacos作为服务注册和发现组件；
3. 重命名handsome-im为ravaland-im-sdk,新增IM群组自定义字段。

v1.0.9
1. 完善IM接口SDK；
2. Web模块新增分组校验，不同分组可配置不同密钥、RSA密钥以及过期时间等； 
3. Token生成和校验支持自动选择RSA模式；
4. 优化框架序列化模块代码结构。

v1.0.8
1. MQListener支持Topic和Tag以配置覆盖;
2. 新增DateConverter注入;
3. 新增IM模块。

v1.0.7
1. 新增Long类型序列化配置，默认转换为String;

v1.0.6
1. 解决Session读取Claim异常的问题;
2. mq模块添加MessageBuilder，用于构建消息体;
3. 修改支付SDK默认网关配置项(基于不同环境)。

v1.0.5
1. 新增支付SDK模块，详情参见文档；
2. 新增MQ模块，详情参见文档；
3. Thrift调用优化。

v1.0.4
1. 解决全局异常捕获，在非正式环境返回具体异常信息；
2. 解决Swagger文档中时间类型显示问题；
3. 优化部分异常日志打印方式。

v1.0.3
1. 解决Thrift多服务的问题；

v1.0.2
1. 添加时间戳类型配置，可选格式化，秒，毫秒，默认秒；
2. 添加Null处理配置，默认关闭；
3. 添加类型辅助方法，常用类型判断等；
4. 解决ResultDTO基础方法data为null的情况；
5. 新增thrift包，适配解决RPC调用。
```

### 3 分钟了解如何进入开发

欢迎使用云效 Codeup，通过阅读以下内容，你可以快速熟悉 Codeup ，并立即开始今天的工作。

### 提交**文件**

首先，你需要了解在 Codeup 中如何提交代码文件，跟着文档「[__
提交第一行代码__](https://thoughts.aliyun.com/sharespace/5e8c37eb546fd9001aee8242/docs/5e8c37e7546fd9001aee81fd)」一起操作试试看吧。

### 开启扫描

开发过程中，为了更好的管理你的代码资产，Codeup 内置了「[__
代码规约扫描__](https://thoughts.aliyun.com/sharespace/5e8c37eb546fd9001aee8242/docs/5e8c37e8546fd9001aee821c)」和「[__敏感信息检测__](https://thoughts.aliyun.com/sharespace/5e8c37eb546fd9001aee8242/docs/5e8c37e8546fd9001aee821b)」服务，你可以在代码库设置-集成与服务中一键开启，开启后提交或合并请求的变更将自动触发扫描，并及时提供结果反馈。

![](https://img.alicdn.com/tfs/TB1nRDatoz1gK0jSZLeXXb9kVXa-1122-380.png "")

![](https://img.alicdn.com/tfs/TB1PrPatXY7gK0jSZKzXXaikpXa-1122-709.png "")

### 代码评审

功能开发完毕后，通常你需要发起「[__
代码合并和评审__](https://thoughts.aliyun.com/sharespace/5e8c37eb546fd9001aee8242/docs/5e8c37e8546fd9001aee8216)」，Codeup
支持多人协作的代码评审服务，你可以通过「[__
保护分支__](https://thoughts.aliyun.com/sharespace/5e8c37eb546fd9001aee8242/docs/5e8c37e9546fd9001aee8221)」策略及「[__合并请求设置__](https://thoughts.aliyun.com/sharespace/5e8c37eb546fd9001aee8242/docs/5e8c37e9546fd9001aee8224)」对合并过程进行流程化管控，同时提供
WebIDE 在线代码评审及冲突解决能力，让你的评审过程更加流畅。

![](https://img.alicdn.com/tfs/TB1XHrctkP2gK0jSZPxXXacQpXa-1432-887.png "")

![](https://img.alicdn.com/tfs/TB1V3fctoY1gK0jSZFMXXaWcVXa-1432-600.png "")

### 编写文档

项目推进过程中，你的经验和感悟可以直接记录到 Codeup 代码库的「[__
文档__](https://thoughts.aliyun.com/sharespace/5e8c37eb546fd9001aee8242/docs/5e8c37e8546fd9001aee8213)」内，让智慧可视化。

![](https://img.alicdn.com/tfs/TB1BN2ateT2gK0jSZFvXXXnFXXa-1432-700.png "")

### 成员协作

是时候邀请成员一起编写卓越的代码工程了，请点击右上角「成员」邀请你的小伙伴开始协作吧！

### 更多

Git 使用教学、高级功能指引等更多说明，参见[__
Codeup帮助文档__](https://thoughts.aliyun.com/sharespace/5e8c37eb546fd9001aee8242/docs/5e8c37e6546fd9001aee81fa)。
