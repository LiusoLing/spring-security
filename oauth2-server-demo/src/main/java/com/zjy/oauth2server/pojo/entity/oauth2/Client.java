package com.zjy.oauth2server.pojo.entity.oauth2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * OAuth2客户端
 * </p>
 *
 * @author LIQIU
 * @since 2018-04-13
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Client implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long createdBy;

    private Date createdDate;

    private Long lastModifiedBy;

    private Date lastModifiedDate;

    private Integer accessTokenValiditySeconds;

    private String clientId;

    private String clientSecret;

    private Integer refreshTokenValiditySeconds;

    /**
     * 授权类型
     */
    private String grantType;

    /**
     * 资源ID
     */
    private String resourceIds;

    /**
     * 跳转URL
     */
    private String redirectUri;

    /**
     * 是否启用
     */
    private Boolean enable;

}
