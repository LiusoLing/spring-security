package com.zjy.oauth2server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author liugenlai
 * @since 2021/7/23 14:18
 */
@SpringBootApplication
@MapperScan("com.zjy.oauth2server.mapper")
public class Oauth2ServerDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(Oauth2ServerDemoApplication.class, args);
    }
}
