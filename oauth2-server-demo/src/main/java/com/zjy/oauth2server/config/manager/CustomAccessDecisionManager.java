package com.zjy.oauth2server.config.manager;

import cn.hutool.core.collection.CollectionUtil;
import com.zjy.oauth2server.pojo.constants.SysConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author liugenlai
 * @since 2021/8/24 10:04
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "zjy.security.oauth2.decision.enabled", havingValue = "true", matchIfMissing = false)
public class CustomAccessDecisionManager implements AccessDecisionManager {

    /**
     * 为传递的参数解析访问控制决策。
     *
     * @param authentication   调用方法的调用者（非空）
     * @param object           被调用的安全对象
     * @param configAttributes 与被调用的安全对象关联的配置属性
     * @throws AccessDeniedException 如果访问被拒绝，因为身份验证不具有所需的权限或 ACL 权限
     * @throws InsufficientAuthenticationException 如果访问被拒绝，因为身份验证没有提供足够的信任级别
     */
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException
            , InsufficientAuthenticationException {
        String requestUrl = ((FilterInvocation) object).getRequest().getRequestURI();
        log.info("requestUrl:{}", requestUrl);
        // 为空则说明数据库未对该接口进行权限配置,不进行处理
        if (CollectionUtil.isEmpty(configAttributes)) {
            return;
        }

        // 设置了允许放行或者Ant表达式
        String first = configAttributes.stream().findFirst().get().getAttribute();
        if (SysConstants.AUTH_ALLOW.equals(first)) {
            return;
        }

        // 当前用户所具有的权限
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (ConfigAttribute attribute : configAttributes) {
            if (this.supports(attribute)) {
                log.debug(">>>>>>>当前账户所拥有的权限：{}" , authorities);
                log.debug(">>>>>>>当前资源所需要的权限:{}" , attribute.getAttribute());
                for (GrantedAuthority authority : authorities) {
                    // 如果当前用户拥有 /** 权限，放行
                    if (authority.getAuthority().equals(SysConstants.DIAGONAL)) {
                        return;
                    }
                    if (attribute.getAttribute().equals(authority.getAuthority())) {
                        return;
                    }
                }

                log.warn("当前用户没有 '{}' 属性.", attribute);
            }
        }

        log.warn("当前请求应至少具有以下属性之一 {}.", configAttributes);

        throw new AccessDeniedException("没有访问权限");
    }


    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
