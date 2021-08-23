package com.zjy.oauth2server.integration;

/**
 * 集成认证参数对象上下文
 * @author liugenlai
 * @since 2021/8/20 15:35
 */
public class IntegrationAuthenticationContext {
    private static ThreadLocal<IntegrationAuthentication> integrationThreadLocal = new ThreadLocal<>();

    /**
     * 添加集成验证对象
     * @param integrationAuthentication
     */
    public static void set(IntegrationAuthentication integrationAuthentication){
        integrationThreadLocal.set(integrationAuthentication);
    }

    /**
     * 获取集成验证对象
     * @return
     */
    public static IntegrationAuthentication get(){
        return integrationThreadLocal.get();
    }

    /**
     * 清除所有集成验证对象
     */
    public static void clear(){
        integrationThreadLocal.remove();
    }
}
