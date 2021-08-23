package com.zjy.oauth2server.exception;

import cn.hutool.json.JSONUtil;
import com.zjy.platform.common.core.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author liugenlai
 * @since 2021/7/26 16:50
 */
@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
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