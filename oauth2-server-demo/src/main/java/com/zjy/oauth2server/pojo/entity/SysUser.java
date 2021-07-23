package com.zjy.oauth2server.pojo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表
 * @author liugenlai
 * @since 2021/7/21 15:14
 */
@Data
public class SysUser implements Serializable {
    private Long id;

    /**
    * 用户名
    */
    private String username;

    /**
    * 密码，加密存储
    */
    private String password;

    /**
    * 注册手机号
    */
    private String phone;

    /**
    * 注册邮箱
    */
    private String email;

    private Date created;

    private Date updated;

    private static final long serialVersionUID = 1L;
}