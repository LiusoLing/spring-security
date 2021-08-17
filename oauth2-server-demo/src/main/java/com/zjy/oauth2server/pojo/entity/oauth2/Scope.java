package com.zjy.oauth2server.pojo.entity.oauth2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * OAuth2客户端授权范围
 * </p>
 *
 * @author LIQIU
 * @since 2018-04-13
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Scope implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 客户端ID
     */
    private Long clientId;

    /**
     * 授权范围
     */
    private String scope;

    /**
     * 自动授权
     */
    private Boolean autoApprove;



}
