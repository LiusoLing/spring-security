package com.zjy.oauth2server.config.token;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author liugenlai
 * @since 2021/7/26 10:41
 */
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "zjy.security.oauth2", name = "type", havingValue = "redis")
public class RedisTokenStore {
    private final RedisConnectionFactory redisConnectionFactory;

    /**
     * 指定令牌管理方式为 Redis
     *
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        return new org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore(redisConnectionFactory);
    }
}
