package com.zjy.oauth2server.config.rsa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;

/**
 * @author liugenlai
 * @since 2021/7/27 8:45
 */
@Configuration
public class KeyPairConfig {

    @Bean
    public KeyPair keyPair() throws Exception {
        // 使用私钥加密，第1个参数就是密钥证书文件，第2个参数 密钥口令(-keypass), 私钥进行签名
        KeyStoreKeyFactory factory = new KeyStoreKeyFactory(new ClassPathResource("oauth2.jks"), "zjy123!".toCharArray());
        // 指定非对称加密 oauth2 别名
        return factory.getKeyPair("oauth2");
    }
}
