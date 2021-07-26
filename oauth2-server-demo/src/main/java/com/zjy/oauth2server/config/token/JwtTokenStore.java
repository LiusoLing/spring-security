package com.zjy.oauth2server.config.token;

import com.zjy.oauth2server.config.converter.CustomUserAuthenticationConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.io.IOException;

/**
 * @author liugenlai
 * @since 2021/7/26 10:41
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "zjy.security.oauth2", name = "type", havingValue = "jwt",matchIfMissing = true)
public class JwtTokenStore {
    /**
     * 指定令牌管理方式
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
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // 使用私钥加密，第1个参数就是密钥证书文件，第2个参数 密钥口令(-keypass), 私钥进行签名
        KeyStoreKeyFactory factory = new KeyStoreKeyFactory(new ClassPathResource("oauth2.jks"), "oauth2".toCharArray());
        // 指定非对称加密 oauth2 别名
        converter.setKeyPair(factory.getKeyPair("oauth2"));
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
        DefaultAccessTokenConverter tokenConverter = (DefaultAccessTokenConverter)converter.getAccessTokenConverter();
        // 想根扩展UserAuthenticationConverter接口，添加用户id字段，但是并没有搞定。
        tokenConverter.setUserTokenConverter(new CustomUserAuthenticationConverter());
        return converter;
    }
}
