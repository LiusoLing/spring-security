package com.zjy.oauth2server.config.provider;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.util.Collection;

/**
 * 前后端分离项目，oauth2 密码模式登录时直接用明文传输用户的密码进行登录存在风险，前端先调oauth默认的获取公钥接口(/oauth/token_key)
 * 拿到公钥，对密码进行加密后再进行传输
 * <p>
 * oauth2 原来的身份认证用的是明文验证，这里需要增加一个AuthenticationProvider身份认证模式实现，先将前端传过来的密码进行 rsa 解密
 * 再和数据库的加密串比对
 *
 * @author liugenlai
 * @since 2021/8/19 14:51
 */
@Component
public class CustomUserAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private KeyPair keyPair;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 认证处理，返回一个Authentication的实现类则代表认证成功，抛出CredentialsExpiredException则失败
     *
     * @param authentication 认证信息
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        // 获取用户信息
        UserDetails user = userDetailsService.loadUserByUsername(username);

        // 比较前端传入的密码和数据库中加密的密码是否相等
        RSA rsa = new RSA(keyPair.getPrivate(), null);
        String plainText = rsa.decryptStr(password, KeyType.PrivateKey);

        if (!passwordEncoder.matches(plainText, user.getPassword())) {
            throw new CredentialsExpiredException("密码不一致");
        }
        // 获取用户权限信息
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        return new UsernamePasswordAuthenticationToken(user, password, authorities);
    }

    /**
     * 如果该AuthenticationProvider支持传入的Authentication对象，则返回true
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(UsernamePasswordAuthenticationToken.class);
    }

}
