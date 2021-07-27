package com.zjy.oauth2server.config;

import com.zjy.oauth2server.exception.CustomAccessDeniedHandler;
import com.zjy.oauth2server.exception.CustomAuthenticationEntryPoint;
import com.zjy.oauth2server.pojo.constants.SecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * 资源配置类
 *
 * @author liugenlai
 * @since 2021/7/23 16:26
 */
@Configuration
@EnableResourceServer()
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    private final SecurityProperties properties;
    private final TokenStore tokenStore;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        System.out.println(properties.getClient().getResourceId());
        // 当前资源服务器的资源id，认证服务会认证客户端有没有访问这个资源id的权限，有则可以访问当前服务
        resources.resourceId(properties.getClient().getResourceId())
                .tokenStore(tokenStore)
                .authenticationEntryPoint(authenticationEntryPoint);
    }
}
