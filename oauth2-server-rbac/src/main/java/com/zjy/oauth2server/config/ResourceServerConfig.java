package com.zjy.oauth2server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * @author liugenlai
 * @since 2021/7/21 16:12
 */
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 匹配oauth相关，匹配健康，匹配默认登录登出 在httpSecurity处理，，其他到ResourceServerConfigurerAdapter OAuth2处理  1
                .requestMatchers().antMatchers("/oauth/**", "/actuator/health", "/login", "/logout")
                .and()
                // 匹配的全部无条件通过 permitAll 2
                .authorizeRequests().antMatchers("/oauth/**", "/actuator/health", "/login", "/logout").permitAll()
                // 匹配条件1的 并且不再条件2通过范围内的其他url全部需要验证登录
                .and().authorizeRequests().anyRequest().authenticated()
                // 启用登录验证
                .and().formLogin().permitAll();
        // 不启用 跨站请求伪造 默认为启用， 需要启用的话得在form表单中生成一个_csrf
        http.csrf().disable();
    }
}
