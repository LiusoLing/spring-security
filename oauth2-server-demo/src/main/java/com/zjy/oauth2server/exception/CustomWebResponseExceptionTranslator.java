package com.zjy.oauth2server.exception;

import com.zjy.platform.common.core.result.Result;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.DefaultThrowableAnalyzer;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InsufficientScopeException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author liugenlai
 * @since 2021/8/19 17:34
 */
@Component
public class CustomWebResponseExceptionTranslator implements WebResponseExceptionTranslator<Result> {
    private ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();

    @Override
    public ResponseEntity<Result> translate(Exception e) throws Exception {
        Throwable[] causeChain = throwableAnalyzer.determineCauseChain(e);
        Exception ase = (OAuth2Exception) throwableAnalyzer.getFirstThrowableOfType(OAuth2Exception.class, causeChain);
        ResponseEntity<OAuth2Exception> responseEntity = handleOAuth2Exception((OAuth2Exception) ase);
        OAuth2Exception body = responseEntity.getBody();
        Result failed = Result.failed();
        failed.setError(body.getLocalizedMessage());
        failed.setData(body.getCause());
        HttpHeaders headers = new HttpHeaders();
        headers.setAll(responseEntity.getHeaders().toSingleValueMap());
        return new ResponseEntity<>(failed, headers, failed.getCode());
    }

    private ResponseEntity<OAuth2Exception> handleOAuth2Exception(OAuth2Exception e) throws IOException {
        int status = e.getHttpErrorCode();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");
        if (status == HttpStatus.UNAUTHORIZED.value() || (e instanceof InsufficientScopeException)) {
            headers.set("WWW-Authenticate", String.format("%s %s", OAuth2AccessToken.BEARER_TYPE, e.getSummary()));
        }

        ResponseEntity<OAuth2Exception> response = new ResponseEntity<OAuth2Exception>(e, headers,
                HttpStatus.valueOf(status));
        return response;
    }
}
