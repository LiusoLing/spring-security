package com.zjy.oauth2server.config.token;

import com.zjy.oauth2server.config.converter.CustomUserAuthenticationConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.io.IOException;
import java.security.KeyPair;

/**
 * @author liugenlai
 * @since 2021/7/26 10:41
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "zjy.security.oauth2", name = "type", havingValue = "jwt", matchIfMissing = true)
@RequiredArgsConstructor
public class JwtTokenStore {
    private final KeyPair keyPair;

    /**
     * 指定令牌管理方式
     *
     * @param jwtAccessTokenConverter
     * @return
     */
    @Bean
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new org.springframework.security.oauth2.provider.token.store.JwtTokenStore(jwtAccessTokenConverter);
    }

    /**
     * 令牌转换器
     * <p>配置公钥和私钥，本来公钥应该是要放到资源服务器里面的./well-known/jwks.json，
     * 但是本项目并不打算以微服务提供，所以就直接集成到这个工程，而不是通过网络连接
     * 所以将公钥和私钥的详细都放在一起了。</p>
     *
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyPair);
        // 使用公钥解密
        ClassPathResource resource = new ClassPathResource("public.txt");
        String publicKey = null;
        try {
            publicKey = IOUtils.toString(resource.getInputStream(), "UTF-8");
        } catch (IOException e) {
            log.info("Caused by: 获取公钥失败{}", JwtTokenStore.class);
            e.printStackTrace();
        }
        converter.setVerifierKey(publicKey);
        DefaultAccessTokenConverter tokenConverter = (DefaultAccessTokenConverter) converter.getAccessTokenConverter();
        // 想根扩展UserAuthenticationConverter接口，添加用户id字段，但是并没有搞定。
        tokenConverter.setUserTokenConverter(new CustomUserAuthenticationConverter());
        return converter;
    }
}
