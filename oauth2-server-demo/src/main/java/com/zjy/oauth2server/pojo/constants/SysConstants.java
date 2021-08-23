package com.zjy.oauth2server.pojo.constants;

import lombok.extern.slf4j.Slf4j;

/**
 * @author liugenlai
 * @since 2021/7/19 16:58
 */
@Slf4j
public class SysConstants {

    /**
     * 验证码redis key
     */
    public static final String CAPTCHA_REDIS_KEY = "%s:captcha:%s";
    /**
     * token黑名单
     */
    public static final String TOKEN_BLACKLIST_PREFIX = "%s:token-blacklist:%s";
    /**
     * 登录认证Header
     */
    public static final String AUTHORIZATION_HEADER = "Authorization";
    /**
     * 斜杆
     */
    public static final String DIAGONAL = "/";
    /**
     * 冒号
     */
    public static final String COLON = ":";

    /**
     * 权限校验放行标识
     */
    public static final String AUTH_ALLOW = "auth_allow";
    /**
     * 游客标识（未登录）
     */
    public static final String VISITOR = "anonymousUser";
    /**
     * 请求方式-post
     */
    public static final String POST = "post";
    /**
     * 登录地址
     */
    public static final String LOGIN = "/authenticate";
    /**
     * 前端传递的验证码参数名
     */
    public static final String CODE = "code";
    /**
     * 前端传递的验证码签名值参数名
     */
    public static final String SIGN = "sign";
}
