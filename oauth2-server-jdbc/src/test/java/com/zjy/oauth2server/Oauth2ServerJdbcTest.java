package com.zjy.oauth2server;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author liugenlai
 * @since 2021/7/21 14:44
 */
public class Oauth2ServerJdbcTest {

    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("secret"));
    }
}
