package com.zjy.oauth2server.exception;

import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

/**
 * @author liugenlai
 * @since 2021/7/23 16:39
 */
@Component
public class SecurityExceptionHandler extends DefaultWebResponseExceptionTranslator {
}
