package com.zjy.oauth2server.config.token;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

/**
 * @author liugenlai
 * @since 2021/7/26 10:40
 */
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "zjy.security.oauth2", name = "type", havingValue = "db")
public class DbTokenStore {
    private final DataSource dataSource;

    /**
     * 指定数据库方式管理 令牌
     *
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }
}
