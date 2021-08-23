//package com.zjy.oauth2server.config.aop;
//
//import cn.hutool.core.bean.BeanUtil;
//import com.zjy.platform.common.core.result.Result;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.stereotype.Component;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * oauth token 返回的不是统一的 Result<T> 风格对象，这里切面进行封装
// * 多方考证发现，修改此接口的返回格式，将导致其他组件不兼容oauth2协议，比如：swagger 自带的认证授权/其他网关组件自带的oauth2等
// * 总体来权衡 弊大于利，所以弃用
// * @author liugenlai
// * @since 2021/8/20 9:00
// */
//@Component
//@Aspect
//public class AuthTokenAspect {
//
//    /**
//     * aop重写oauth2的TokenEndpoint.postAccessToken结果
//     * @param pjp
//     * @return
//     * @throws Throwable
//     */
//    @Around("execution(* org.springframework.security.oauth2.provider.endpoint.TokenEndpoint.postAccessToken(..))")
//    public Object handlePostAccessToken(ProceedingJoinPoint pjp) throws Throwable {
//        // 放行
//        Object proceed = pjp.proceed();
//        OAuth2AccessToken body = null;
//        if (proceed != null) {
//            ResponseEntity<OAuth2AccessToken> responseEntity = (ResponseEntity<OAuth2AccessToken>) proceed;
//            body = responseEntity.getBody();
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(Result.success(body));
//    }
//
//    /**
//     * aop重写oauth2的CheckTokenEndpoint.checkToken结果
//     * @param pjp
//     * @return
//     * @throws Throwable
//     */
//    @Around("execution(* org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint.checkToken(..))")
//    public Object handleCheckToken(ProceedingJoinPoint pjp) throws Throwable {
//        // 放行
//        Object proceed = pjp.proceed();
//        Map<String, Object> body = new HashMap<>();
//        if (proceed != null) {
//            body = (HashMap) proceed;
//        }
//        return BeanUtil.beanToMap(Result.success(body));
//    }
//}
