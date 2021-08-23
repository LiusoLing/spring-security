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
        log.error("请求访问：{} 异常", request.getRequestURI());
        log.error("e: {}", e);
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        Result<Object> failed = Result.failed();
        failed.setError(e.getLocalizedMessage());
        failed.setData(e.getCause());
        response.getWriter().print(JSONUtil.toJsonStr(failed));
    }
}

