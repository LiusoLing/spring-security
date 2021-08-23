package com.zjy.oauth2server.integration;

import lombok.Data;

import java.util.Map;

/**
 * 集成认证参数对象
 * @author liugenlai
 * @since 2021/8/20 15:29
 */
@Data
public class IntegrationAuthentication {
    /**
     * 授权类型：captcha、sms、email......
     */
    private String authType;
    /**
     * 用户名
     */
    private String username;
    /**
     * 授权参数
     */
    private Map<String,String[]> authParameters;

    public String getAuthParameter(String param){
        String[] values = this.authParameters.get(param);
        if(values != null && values.length > 0){
            return values[0];
        }
        return null;
    }
}
