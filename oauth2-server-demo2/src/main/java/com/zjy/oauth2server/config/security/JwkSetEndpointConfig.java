package com.zjy.oauth2server.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerSecurityConfiguration;

/**
 * JWKS 接入点安全配置
 * 配置 ./well-known/jwks.json 允许公开访问
 * 存在多个 Security 配置的情况下，需要设置不同的顺序，order 是必须的
 * @author liusongling
 * @since 2021-07-25 19:11:23
 */
@Order(1)
@Configuration
public class JwkSetEndpointConfig extends AuthorizationServerSecurityConfiguration {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .requestMatchers(req -> req.mvcMatchers("/.well-known/jwks.json"))
                .authorizeRequests(req -> req.mvcMatchers("/.well-known/jwks.json").permitAll());
    }
}
