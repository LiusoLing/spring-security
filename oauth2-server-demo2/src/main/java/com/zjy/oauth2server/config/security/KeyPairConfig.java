package com.zjy.oauth2server.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;

/**
 * @author liusongling
 * @since 2021-07-25 19:53:00
 */
@Configuration
public class KeyPairConfig {

    @Bean
    public KeyPair keyPair() {
        ClassPathResource pathResource = new ClassPathResource("zjy-oauth2-demo-jwt.jks");
        KeyStoreKeyFactory keyFactory = new KeyStoreKeyFactory(pathResource, "zjy-pass".toCharArray());
        return keyFactory.getKeyPair("zjy-oauth2-jwt");
    }
}
