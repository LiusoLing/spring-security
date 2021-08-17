package com.zjy.oauth2server.config;

import com.zjy.oauth2server.config.service.CustomUserDetailsService;
import com.zjy.oauth2server.exception.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;

/**
 * 认证授权 配置类
 * 核心：
 * AuthorizationServerSecurityConfigurer：安全功能配置器
 * AuthorizationServerEndpointsConfigurer：端点配置器（配置授权服务器端点的非安全功能，如令牌存储、令牌定制，用户批准和授权类型等）
 * ClientDetailsServiceConfigurer：客户端信息配置器
 * @author liugenlai
 * @since 2021/7/23 16:25
 */
@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    // private final IntegrationAuthenticationFilter authenticationFilter;
    private final TokenStore tokenStore;
    private final DataSource dataSource;
    @Autowired(required = false)
    private JwtAccessTokenConverter jwtAccessTokenConverter;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final PasswordEncoder passwordEncoder;


    /**
     * 从数据库获取授权码策略：
     * <p>authorizedGrantTypes授权类型, 可同时支持多种授权类型
     * <ul>
     *     <li>authorization_code：授权码模式</li>
     *     <li>password：密码模式</li>
     *     <li>implicit：简化模式</li>
     *     <li>client_credentials：客户端模式</li>
     *     <li>refresh_token：刷新令牌</li>
     * </ul>
     * </p>
     *
     * @return
     */
    @Bean
    public JdbcAuthorizationCodeServices jdbcAuthorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    /**
     * 使用 jdbcClientDetailsService
     *
     * @return
     */
    @Bean
    public ClientDetailsService jdbcClientDetailsService() {
        JdbcClientDetailsService service = new JdbcClientDetailsService(dataSource);
        service.setPasswordEncoder(passwordEncoder);
        return service;
    }

    /**
     * 允许访问此认证服务器的客户端详情信息配置
     * 方式1：内存方式管理（inMemory/redis）
     * 方式2：数据库管理（db）
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(jdbcClientDetailsService());
    }

    /**
     * 认证服务器端点配置
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                // 设置 token管理方式
                .tokenStore(tokenStore)
                // password 需要 manager 验证器
                .authenticationManager(authenticationManager)
                // 刷新令牌时需要用到 userDetailService
                .userDetailsService(userDetailsService)
                // 授权码管理策略，针对授权码模式有效，会将授权码放到 auth_code 表，授权后就会删除它
                .authorizationCodeServices(jdbcAuthorizationCodeServices())
                // 禁止重复使用 refreshToken
                .reuseRefreshTokens(false);
        // 判断是否使用了 jwt令牌认证 模式
        if (jwtAccessTokenConverter != null) {
            // 指定JWT转换器 accessTokenConverter
            endpoints.accessTokenConverter(jwtAccessTokenConverter);
        }
    }

    /**
     * 端点的安全配置
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients()
                // 获取公钥端点，默认拒绝访问，需要公开
                .tokenKeyAccess("permitAll()")
                // 检查 token 端点，需要认证后才允许访问，默认拒绝访问
                .checkTokenAccess("isAuthenticated()")
                .authenticationEntryPoint(authenticationEntryPoint);
        //.addTokenEndpointAuthenticationFilter(authenticationFilter);
    }
}
