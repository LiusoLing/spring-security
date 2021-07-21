package com.zjy.oauth2server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

/**
 * @author liugenlai
 * @since 2021/7/21 14:11
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public TokenStore tokenStore() {
        // base jdbc achieve, token save to datasource
        return new JdbcTokenStore(dataSource());
    }

    @Bean
    public ClientDetailsService jdbcClientDetails() {
        // base jdbc achieve, compare client message on datasource
        return new JdbcClientDetailsService(dataSource());
    }


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // read client config on the datasource
        clients.withClientDetails(jdbcClientDetails());
    }
}
