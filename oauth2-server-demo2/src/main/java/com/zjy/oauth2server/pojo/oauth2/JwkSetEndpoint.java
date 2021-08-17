package com.zjy.oauth2server.pojo.oauth2;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

/**
 * 为了与 spring security 5.1 以上版本的 Resource server 兼容
 * 需要让 spring security oauth2 支持 JWK
 * 此类 暴露 JWK 的接入点
 * @FrameworkEndpoint 表示是框架级别的接入点
 * @author liusongling
 * @since 2021-07-25 19:20:43
 */
@FrameworkEndpoint
@RequiredArgsConstructor
public class JwkSetEndpoint {
    private final KeyPair keyPair;

    @GetMapping("/.well-known/jwks.json")
    @ResponseBody
    public Map<String, Object> getKey() {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAKey key = new RSAKey.Builder(publicKey).build();
        return new JWKSet(key).toJSONObject();
    }
}
