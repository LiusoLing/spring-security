package com.zjy.oauth2server.integration;

import com.zjy.oauth2server.pojo.entity.oauth2.SysUserAuthentication;

/**
 * 集成验证器
 * @author liugenlai
 * @since 2021/8/20 15:27
 */
public interface IntegrationAuthenticator {

    /**
     * 进行预处理
     * @param integrationAuthentication
     */
    void prepare(IntegrationAuthentication integrationAuthentication);

    /**
     * 判断是否支持集成认证类型
     * @param integrationAuthentication
     * @return
     */
    boolean support(IntegrationAuthentication integrationAuthentication);

    /**
     * 处理集成认证
     * @param integrationAuthentication
     * @return
     */
    SysUserAuthentication authenticate(IntegrationAuthentication integrationAuthentication);

    /** 认证结束后执行
     * @param integrationAuthentication
     */
    void complete(IntegrationAuthentication integrationAuthentication);
}
