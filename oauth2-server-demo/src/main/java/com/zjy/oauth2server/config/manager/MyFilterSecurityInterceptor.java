//package com.zjy.oauth2server.config.manager;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.security.access.SecurityMetadataSource;
//import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
//import org.springframework.security.access.intercept.InterceptorStatusToken;
//import org.springframework.security.web.FilterInvocation;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.*;
//import java.io.IOException;
//
///**
// * 获取用户的权限和获取url路径的权限提供给决策器(AccessDecisionManager)使用
// *
// * @author liugenlai
// * @since 2021/8/24 15:18
// */
//@Slf4j
//@Component
//@RequiredArgsConstructor
//@ConditionalOnProperty(name = "zjy.security.oauth2.decision.enabled", havingValue = "ture")
//public class MyFilterSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {
//    private final CustomSecurityMetadataSource securityMetadataSource;
//    private final CustomAccessDecisionManager accessDecisionManager;
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        FilterInvocation fi = new FilterInvocation(request, response, chain);
//        invoke(fi);
//    }
//
//    private void invoke(FilterInvocation fi) throws IOException, ServletException {
//        //fi里面有一个被拦截的url
//        //里面调用MyInvocationSecurityMetadataSource的getAttributes(Object object)这个方法获取fi对应的所有权限
//        //再调用MyAccessDecisionManager的decide方法来校验用户的权限是否足够
//        InterceptorStatusToken token = super.beforeInvocation(fi);
//        try {
//            //执行下一个拦截器
//            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
//        } finally {
//            super.afterInvocation(token, null);
//        }
//    }
//
//    @Override
//    public void destroy() {
//
//    }
//
//    @Override
//    public Class<?> getSecureObjectClass() {
//        return FilterInvocation.class;
//    }
//
//    @Override
//    public SecurityMetadataSource obtainSecurityMetadataSource() {
//        return this.securityMetadataSource;
//    }
//
//    // 设置自定义的AccessDecisionManager
//    @Autowired
//    public void setAccessDecisionManager(CustomAccessDecisionManager accessDecisionManager) {
//        super.setAccessDecisionManager(accessDecisionManager);
//    }
//}
