package com.zjy.oauth2server.pojo.entity.oauth2;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * oauth_client_details
 * @author 
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("oauth_client_details")
public class Client implements Serializable {
    /**
     * 客户端ID
     */
    @TableId(value = "client_id", type = IdType.INPUT)
    private String clientId;
    /**
     * 资源ID
     */
    private String resourceIds;
    /**
     * 客户端秘钥
     */
    private String clientSecret;
    /**
     * 授权范围
     */
    private String scope;
    /**
     * 认证方式
     */
    private String authorizedGrantTypes;
    /**
     * 回调地址
     */
    private String webServerRedirectUri;
    /**
     * 权限
     */
    private String authorities;
    /**
     * token有效期
     */
    private Integer accessTokenValidity;
    /**
     * refreshToken有效期
     */
    private Integer refreshTokenValidity;
    /**
     * 额外的信息
     */
    private String additionalInformation;
    /**
     * 自动授权
     */
    private String autoapprove;

    private static final long serialVersionUID = 1L;
}