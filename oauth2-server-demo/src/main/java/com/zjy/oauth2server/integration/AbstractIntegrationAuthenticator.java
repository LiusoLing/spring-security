package com.zjy.oauth2server.integration;

import com.zjy.oauth2server.pojo.entity.oauth2.SysUserAuthentication;

/**
 * @author liugenlai
 * @since 2021/8/23 18:27
 */
public abstract class AbstractIntegrationAuthenticator implements IntegrationAuthenticator{

    /**
     * 进行预处理
     *
     * @param integrationAuthentication
     */
    @Override
    public abstract void prepare(IntegrationAuthentication integrationAuthentication);

    /**
     * 判断是否支持集成认证类型
     *
     * @param integrationAuthentication
     * @return
     */
    @Override
    public abstract boolean support(IntegrationAuthentication integrationAuthentication);

    /**
     * 处理集成认证
     *
     * @param integrationAuthentication
     * @return
     */
    @Override
    public abstract SysUserAuthentication authenticate(IntegrationAuthentication integrationAuthentication);

    /**
     * 认证结束后执行
     *
     * @param integrationAuthentication
     */
    @Override
    public void complete(IntegrationAuthentication integrationAuthentication) {

    }
}
