package com.zjy.oauth2server.exception;

import cn.hutool.json.JSONUtil;
import com.zjy.platform.common.core.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author liugenlai
 * @since 2021/7/26 16:14
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint extends OAuth2AuthenticationEntryPoint {
    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException {
        log.error("请求访问：{}，认证失败，无法访问系统资源", request.getRequestURI());
        log.error("e: {}", e);
        response.setStatus(200);
        // 允许跨域
        response.setHeader("Access-Control-Allow-Origin", "*");
        // 允许自定义请求头token(允许head跨域)
        response.setHeader("Access-Control-Allow-Headers", "Authorization, authorization,token, Accept, Origin, X-Requested-With, Content-Type, Last-Modified");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().print(JSONUtil.toJsonStr(Result.failed("认证失败，无法访问系统资源")));
    }
}

