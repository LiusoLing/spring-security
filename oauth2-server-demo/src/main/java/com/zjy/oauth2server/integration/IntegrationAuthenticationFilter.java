package com.zjy.oauth2server.integration;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Security filter chain: [
 *   WebAsyncManagerIntegrationFilter：
 *          提供了对securityContext和WebAsyncManager的集成。其实就是把SecurityContext设置到异步线程中，使其也能获取到用户上下文认证信息。
 *   SecurityContextPersistenceFilter：
 *          在请求之前从SecurityContextRepository（默认实现是HttpSessionSecurityContextRepository）获取信息并填充SecurityContextHolder（
 *          如果没有，则创建一个新的ThreadLocal的SecurityContext），并在请求完成时并清空SecurityContextHolder并更新SecurityContextRepository。
 *   HeaderWriterFilter：
 *          用来给http响应添加一些Header，比如X-Frame-Options，X-XSS-Protection*，X-Content-Type-Options。
 *   LogoutFilter：
 *          处理注销的过滤器
 *   ClientCredentialsTokenEndpointFilter：
 *          表单提交了username和password，被封装成UsernamePasswordAuthenticationToken对象进行一系列的认证，便是主要通过这个过滤器完成的，
 *          即调用AuthenticationManager.authenticate()。在表单认证的方法中，这是最最关键的过滤器。具体过程是：
 *              （1）调用AbstractAuthenticationProcessingFilter.doFilter()方法执行过滤器
 *              （2）调用UsernamePasswordAuthenticationFilter.attemptAuthentication()方法
 *              （3）调用AuthenticationManager.authenticate()方法（实际上委托给AuthenticationProvider的实现类来处理）
 *   BasicAuthenticationFilter：
 *          处理 HTTP 请求的 BASIC 授权标头，将结果放入 SecurityContextHolder。
 *   RequestCacheAwareFilter：
 *          内部维护了一个RequestCache，用于缓存request请求
 *   SecurityContextHolderAwareRequestFilter：
 *          此过滤器对ServletRequest进行了一次包装，使得request具有更加丰富的API
 *   AnonymousAuthenticationFilter：
 *          匿名身份过滤器，spring security为了兼容未登录的访问，也走了一套认证流程，只不过是一个匿名的身份。它位于身份认证过滤器（e.g. UsernamePasswordAuthenticationFilter）之后，
 *          意味着只有在上述身份过滤器执行完毕后，SecurityContext依旧没有用户信息，AnonymousAuthenticationFilter该过滤器才会有意义。
 *   SessionManagementFilter：
 *          和session相关的过滤器，内部维护了一个SessionAuthenticationStrategy来执行任何与session相关的活动，
 *          比如session-fixation protection mechanisms or checking for multiple concurrent logins。
 *   ExceptionTranslationFilter：
 *          异常转换过滤器，这个过滤器本身不处理异常，而是将认证过程中出现的异常（AccessDeniedException and AuthenticationException）交给内部维护的一些类去处理。它
 *          位于整个springSecurityFilterChain的后方，用来转换整个链路中出现的异常，将其转化，顾名思义，转化以意味本身并不处理。一般其只处理两大类异常：
 *          AccessDeniedException访问异常和AuthenticationException认证异常。
 *          如果该过滤器检测到AuthenticationException，则将会交给内部的AuthenticationEntryPoint去处理，如果检测到AccessDeniedException，需要先判断当前用户是不是匿名用户，
 *          如果是匿名访问，则和前面一样运行AuthenticationEntryPoint，否则会委托给AccessDeniedHandler去处理，而AccessDeniedHandler的默认实现，是AccessDeniedHandlerImpl。
 *   FilterSecurityInterceptor：
 *          这个过滤器决定了访问特定路径应该具备的权限，这些受限的资源访需要什么权限或角色，这些判断和处理都是由该类进行的。
 *            （1）调用FilterSecurityInterceptor.invoke()方法执行过滤器
 *            （2）调用AbstractSecurityInterceptor.beforeInvocation()方法
 *            （3）调用AccessDecisionManager.decide()方法决策判断是否有该权限
 * ]
 * 上述是整个security 的过滤器链和各个过滤器的作用
 * AuthorizationServerSecurityConfigurer 的 addTokenEndpointAuthenticationFilter 方法可以向该过滤器链中添加过滤器
 * 而且添加的任何过滤器都将插入BasicAuthenticationFilter之前
 *
 * <b>集成验证过滤器链</b>，可以包含多种可插拔的登录校验方式：验证码、短信、邮件等
 *
 * @author liugenlai
 * @since 2021/7/23 16:56
 */
@Component
public class IntegrationAuthenticationFilter extends GenericFilterBean {
    private static final String AUTH_TYPE_PARAM_NAME = "auth_type";
    private static final String OAUTH_TOKEN_URL = "/oauth/token";
    private Collection<IntegrationAuthenticator> authenticators;
    private RequestMatcher requestMatcher;

    public IntegrationAuthenticationFilter() {
        this.requestMatcher = new OrRequestMatcher(
                new AntPathRequestMatcher(OAUTH_TOKEN_URL, "GET"),
                new AntPathRequestMatcher(OAUTH_TOKEN_URL, "POST")
        );
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if(requestMatcher.matches(request)){
            //设置集成登录信息
            IntegrationAuthentication integrationAuthentication = new IntegrationAuthentication();
            integrationAuthentication.setAuthType(request.getParameter(AUTH_TYPE_PARAM_NAME));
            integrationAuthentication.setAuthParameters(request.getParameterMap());
            IntegrationAuthenticationContext.set(integrationAuthentication);
            try{
                //预处理
                this.prepare(integrationAuthentication);

                filterChain.doFilter(request,response);

                //后置处理
                this.complete(integrationAuthentication);
            }finally {
                IntegrationAuthenticationContext.clear();
            }
        }else{
            filterChain.doFilter(request,response);
        }
    }

    /**
     * 预处理，获取容器中所有的验证器
     * @param integrationAuthentication 集成认证参数对象
     */
    private void prepare(IntegrationAuthentication integrationAuthentication) {
        //WebApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();
        //Map<String, IntegrationAuthenticator> map = applicationContext.getBeansOfType(IntegrationAuthenticator.class);
        Map<String, IntegrationAuthenticator> map = SpringUtil.getBeansOfType(IntegrationAuthenticator.class);
        if(map != null){
            this.authenticators = map.values();
        }
        if(this.authenticators == null){
            this.authenticators = new ArrayList<>();
        }

        // 执行验证器预处理
        for (IntegrationAuthenticator authenticator: authenticators) {
            if(authenticator.support(integrationAuthentication)){
                authenticator.prepare(integrationAuthentication);
            }
        }
    }

    /**
     * 后置处理
     * @param integrationAuthentication
     */
    private void complete(IntegrationAuthentication integrationAuthentication) {
        for (IntegrationAuthenticator authenticator: authenticators) {
            if(authenticator.support(integrationAuthentication)){
                // 执行验证器后置处理
                authenticator.complete(integrationAuthentication);
            }
        }
    }
}
