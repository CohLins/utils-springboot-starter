# utils-springboot-starter

#### 介绍
一个关于Mybatis和spring的公共组件starter，包括数据自动加解密、数据脱敏、SQL执行日志、请求日志等功能，后续可能考虑加入更多比如限流、熔断、RPC调用等等

#### 软件架构
软件架构说明




#### 使用说明

![输入图片说明](https://foruda.gitee.com/images/1679734863481832353/7bb9c8a4_6577380.png "屏幕截图")

- request-log: 开启请求日志打印
- sql-log: 开启SQL执行日志打印
- aes-key: RES加解密key（必须为16位）
- rsa-private-key: RSA加解密  私钥
- rsa-public-key:  RSA加解密  公钥

#### 注解使用



- 请求日志需在接口层面打上@RequestLog注解
- 数据脱敏需要在接口层面打上@DataDesensitization注解，同时需要在返回的数据实体类上对需要脱敏的字段打上@DataDesensitizationMode注解
- 数据加密，需要在Mapper层面对插入或更新方法上打上@EncryptAndDecry注解，并配置需要加密的字段(实体类属性名)以及加密方式(MD5，RSA，AES)
- 数据解密，需要在Mapper层面对插入或更新方法上打上@EncryptAndDecry注解，并配置需要解密的字段(实体类属性名)以及解密方式(MD5，RSA，AES)


#### 注解说明

1. @DataDesensitizationMode 内置了几种常见的脱敏类型，如姓名、地址、手机号、邮箱、身份证等，也可以自定义，自定义分两种：
- 字符替换为"*"：自己配置需要替换的字符串区间 [startInclude,endExclude]
- 正则替换：自己配置正则表达式（regex）,替换匹配的内容为（replacedChar ，默认"*"）

2. @EncryptAndDecry 内置三种加解密方式:
- MD5：只加密，解密不可逆
- AES：加解密对称，需要配置16位的key
- RSA：加解密不对称，需要配置公钥和私钥




    



