package com.zjy.oauth2server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.zjy.oauth2server.mapper")
public class Oauth2ServerRbacApplication {

    public static void main(String[] args) {
        SpringApplication.run(Oauth2ServerRbacApplication.class, args);
    }

}