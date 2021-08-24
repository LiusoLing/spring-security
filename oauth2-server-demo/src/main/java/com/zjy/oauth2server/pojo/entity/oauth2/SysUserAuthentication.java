package com.zjy.oauth2server.pojo.entity.oauth2;

import lombok.Data;

import java.io.Serializable;

/**
 * oauth需要的基本 sysUser对象，rbac体系的用户表可以继承它进行扩展
 * @author liugenlai
 * @since 2021/8/23 14:25
 */
@Data
public class SysUserAuthentication implements Serializable {
    private static final long serialVersionUID = 5716097884090399040L;

    private Long id;

    private String username;

    private String password;

    private String email;

    private String mobile;

    private String status;

    private String nickname;

    private String type;
}
