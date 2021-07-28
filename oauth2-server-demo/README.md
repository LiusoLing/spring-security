目标： 
1、tokenStore 可配置，自由选择想要的 token 存储方式：数据库存储，jwt存储，Redis缓存存储 
2、JwtToken模式下秘钥、密码采用 RSA 非对称加密算法 
3、clientService 支持多种授权模式：授权码授权、密码模式、客户端模式、简单模式 
4、统一封装返回oauth2错误异常提示信息 
5、有管理 所有需认证客户端 的能力（新增、修改、删除、查询） 
6、保证任何模式下的 /oauth/token、/oauth/token_key、/oauth/refresh_key、/oauth/check_token，接口能正常工作 
7、密码模式下，支持可插入 图形验证码、短信验证码、邮件验证码等 认证因子进行认证，且认证因子可配置（需自己实现） 
8、没有被权限表或权限注解管理起来的接口，做不到不能被访问，会被所有已登录的人访问到 
9、退出登录后需要删除所有的凭证信息，凭证过期后删除过期凭证 
10、验证是否通过认证授权的 scope 域 获取 对应的权限列表来限制能访问到的资源

版本：

> spring-boot  2.4.5 + spring-security + spring-security-oauth2 + mysql + mybatis



关键Pom依赖：

```xml

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
<groupId>org.springframework.security.oauth.boot</groupId>
<artifactId>spring-security-oauth2-autoconfigure</artifactId>
<version>2.2.2.RELEASE</version>
</dependency>
        <!-- 新版 Resource Server 类库 -->
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>
```

如何食用：

新建项目 Pom 中引入本项目：

```xml

<dependency>
    <groupId>com.zjy</groupId>
    <artifactId>oauth2-server-demo</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>    
```

application.yml 文件支持的配置：

```yaml
zjy:
  security:
    oauth2:
      # 支持三种 token 的管理方式：db 数据库管理，jwtToken，redis缓存管理
      type: db/jwt/redis
      # 默认不需要配置，配置要认证的 url
      http-urls: [ "url1", "url2" ]
      # 默认不需要配置，url 权限配置
      url-permission:
        # 是否开启 URL 级别权限
        enable: false
        # 白名单，配置需要url权限认证的应用id
        includeClientIds: [ "","" ]
        # 黑名单，配置不需要url权限认证的应用id (与白名单互斥，只能配置其中一个)
        exclusiveClientIds: [ "","" ]
        # 配置只进行登录认证，不进行url权限认证的api，所有已登录的人都能访问的api
        ignore-urls: [ "","" ]
    code:
      # 默认不需要配置，设置认证通时不需要验证码的clientId
      ignore-client-code: [ "","" ]
    client:
      # 默认不需要配置，当前资源服务器的资源id，认证服务会认证客户端有没有访问这个资源id的权限，有则可以访问当前服务
      resource-id: ""
```

RSA 公钥私钥

生成秘钥对（密码 zjy123!  别名 oauth2）

```shell
keytool -genkeypair -alias oauth2 -keyalg RSA -keypass zjy123! -keystore oauth2.jks -storepass zjy123! -keystore oauth2.jks
```

查看秘钥对

```shell
keytool -list -rfc --keystore oauth2.jks | openssl x509 -inform pem -pubkey
```

查看秘钥信息

```shell
keytool -list -v -keystore oauth2.jks
```