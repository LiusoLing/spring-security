package com.zjy.oauth2server.pojo.constants;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限配置类，所有权限的相关配置都归纳在这里
 * @author liugenlai
 * @since 2021/7/26 11:17
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "zjy.security")
public class SecurityProperties {

    /**
     * 认证相关配置
     */
    private Oauth2Properties oauth2 = new Oauth2Properties();

    /**
     * 验证码相关
     */
    private ValidateCodeProperties code = new ValidateCodeProperties();

    /**
     * 客户端先关信息
     */
    private ClientProperties client = new ClientProperties() ;

    @Setter
    @Getter
    public static class Oauth2Properties {
        // 配置要认证的url（默认不需要配置）
        // 优先级大于忽略认证配置`zlt.security.ignore.httpUrls`,如果同一个url同时配置了`忽略认证`和`需要认证`，则该url还是会被认证
        private String[] httpUrls = {};

        // url权限配置
        private UrlPermissionProperties urlPermission = new UrlPermissionProperties();

        // token管理方式: db、jwt、redis
        private String type ;
    }

    @Getter
    public static class UrlPermissionProperties {
        // 是否开启url级别权限
        private Boolean enable = false;

        // 白名单，配置需要url权限认证的应用id（与黑名单互斥，只能配置其中一个），不配置默认所有应用都生效,配置enable为true时才生效
        private List<String> includeClientIds = new ArrayList<>();

        // 黑名单，配置不需要url权限认证的应用id（与白名单互斥，只能配置其中一个）配置enable为true时才生效
        private List<String> exclusiveClientIds = new ArrayList<>();

        // 配置只进行登录认证，不进行url权限认证的api，所有已登录的人都能访问的api
        private String[] ignoreUrls = {};
    }

    @Setter
    @Getter
    public class ValidateCodeProperties {
        // 设置认证通时不需要验证码的clientId
        private String[] ignoreClientCode = {};
    }

    @Getter
    @Setter
    public class ClientProperties {
        // 当前资源服务器的资源id，认证服务会认证客户端有没有访问这个资源id的权限，有则可以访问当前服务
        private String resourceId = "product";
    }
}
