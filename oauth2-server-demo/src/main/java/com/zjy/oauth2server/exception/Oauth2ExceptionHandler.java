package com.zjy.oauth2server.exception;

import com.zjy.platform.common.core.result.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局处理Oauth2抛出的异常
 *
 * @author liugenlai
 * @since 2021/7/26 15:56
 */
@ControllerAdvice
public class Oauth2ExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = OAuth2Exception.class)
    public Result handleOauth2(OAuth2Exception e) {
        return Result.failed(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = AccessDeniedException.class)
    public Result handleAccessDeniedException(AccessDeniedException e) {
        return Result.failed(e.getMessage());
    }
}
