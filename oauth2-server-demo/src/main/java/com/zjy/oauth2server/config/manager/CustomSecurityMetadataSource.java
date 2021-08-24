package com.zjy.oauth2server.config.manager;

import com.zjy.oauth2server.pojo.constants.SecurityProperties;
import com.zjy.oauth2server.pojo.constants.SysConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * @author liugenlai
 * @since 2021/8/24 14:16
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "zjy.security.oauth2.decision.enabled", havingValue = "true")
public class CustomSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    /**
     * ant风格的路径匹配器
     */
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private final SecurityProperties securityProperties;

    /**
     * supports返回值设成true表示支持
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        HttpServletRequest request = ((FilterInvocation) object).getRequest();
        //获取当前用户请求的url
        String requestUrl = request.getRequestURI();

        Collection<ConfigAttribute> configAttributes = blankProcess(request);
        if (configAttributes != null) {
            return configAttributes;
        }

        return SecurityConfig.createList(requestUrl);
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    private Collection<ConfigAttribute> blankProcess(HttpServletRequest request) {
        String requestUrl = request.getRequestURI();

        //白名单接口
        for (String allow : securityProperties.getOauth2().getUrlPermission().getIgnoreUrls()) {
            if (pathMatcher.match(allow, requestUrl)) {
                return SecurityConfig.createList(SysConstants.AUTH_ALLOW);
            }
        }
        return null;
    }

}