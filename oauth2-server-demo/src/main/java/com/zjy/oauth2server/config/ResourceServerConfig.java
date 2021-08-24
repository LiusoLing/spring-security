package com.zjy.oauth2server.config;

import com.zjy.oauth2server.config.manager.CustomAccessDecisionManager;
import com.zjy.oauth2server.config.manager.CustomSecurityMetadataSource;
import com.zjy.oauth2server.exception.CustomAccessDeniedHandler;
import com.zjy.oauth2server.exception.CustomAuthenticationEntryPoint;
import com.zjy.oauth2server.pojo.constants.SecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

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
    @Autowired(required = false)
    private CustomAccessDecisionManager accessDecisionManager;
    @Autowired(required = false)
    private CustomSecurityMetadataSource metadataSource;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        System.out.println(properties.getClient().getResourceId());
        // 当前资源服务器的资源id，认证服务会认证客户端有没有访问这个资源id的权限，有则可以访问当前服务
        resources.resourceId(properties.getClient().getResourceId())
                .tokenStore(tokenStore)
                // 资源服务器认证异常信息处理：配置OAuth2AuthenticationEntryPoint自定义异常类，并重写commence方法返回自定义Oauth2认证异常信息
                .authenticationEntryPoint(authenticationEntryPoint)
                // 资源服务器权限异常信息处理：配置AccessDeniedHandler自定义异常类，并重写handle方法返回自定义oauth2权限异常信息
                .accessDeniedHandler(accessDeniedHandler)
                .stateless(false);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        if (accessDecisionManager != null) {
            // 重写权限判断
            http.requestMatchers().anyRequest().and().authorizeRequests()
                    .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                        @Override
                        public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                            object.setSecurityMetadataSource(metadataSource);
                            object.setAccessDecisionManager(accessDecisionManager);
                            return object;
                        }
                    }).anyRequest().authenticated();
        }
    }
}
