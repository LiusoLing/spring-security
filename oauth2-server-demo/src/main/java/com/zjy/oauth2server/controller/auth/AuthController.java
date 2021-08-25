package com.zjy.oauth2server.controller.auth;

import cn.hutool.core.util.StrUtil;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import com.zjy.oauth2server.pojo.constants.SecurityProperties;
import com.zjy.oauth2server.pojo.constants.SysConstants;
import com.zjy.platform.common.core.result.Result;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.KeyPair;
import java.security.Principal;
import java.util.concurrent.TimeUnit;

/**
 * @author liugenlai
 * @since 2021/7/23 17:13
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final RedisTemplate<String, String> redisTemplate;
    private final KeyPair keyPair;
    private final SecurityProperties securityProperties;
    private final ConsumerTokenServices consumerTokenServices;
    private final TokenStore tokenStore;
    private final ConsumerTokenServices tokenServices;

    @GetMapping("/current")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    @ApiOperation("当前登录用户信息")
    public Result userInfo(Principal principal) {
        return Result.success(principal);
    }

    @GetMapping("/hello")
    @PreAuthorize("hasRole('operator')")
    public Result hello() {
        return Result.success("Hello world");
    }

    @GetMapping("/captcha")
    @ApiOperation("获取验证码")
    public void captcha(String codeId, HttpServletResponse response) throws Exception {
        // 设置请求头为输出图片类型
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        // 三个参数分别为宽、高、位数
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        // 设置类型，纯数字、纯字母、字母数字混合
        specCaptcha.setCharType(Captcha.TYPE_NUM_AND_UPPER);

        // 验证码存入缓存，有效时间2分钟
        redisTemplate.opsForValue().set(String.format(SysConstants.CAPTCHA_REDIS_KEY, securityProperties.getApplicationName(), codeId),
                specCaptcha.text(), 2, TimeUnit.MINUTES);
        // 输出图片流
        specCaptcha.out(response.getOutputStream());
    }

    @GetMapping("/oauth/logout")
    public Result oauthLogout(HttpServletRequest request) {
        String token = request.getHeader(SysConstants.AUTHORIZATION_HEADER);
        if(Strings.isBlank(token) || !token.startsWith(OAuth2AccessToken.BEARER_TYPE)){
            log.error("token为空或者不是Bearer类型");
            return Result.failed("登出失败");
        }
        token = token.replace("Bearer ", "");
        OAuth2AccessToken accessToken = tokenStore.readAccessToken(token);
        if (accessToken.getAdditionalInformation().containsKey("jti")) {
            // JWT唯一标识
            String jti = accessToken.getAdditionalInformation().get("jti").toString();
            String key = String.format(SysConstants.TOKEN_BLACKLIST_PREFIX, securityProperties.getApplicationName(), jti);
            String redisToken = redisTemplate.opsForValue().get(key);
            // JWT过期时间戳(单位:秒)
            long exp = accessToken.getExpiration().getTime() / 1000;
            long currentTimeSeconds = System.currentTimeMillis() / 1000;

            if (StrUtil.isNotBlank(redisToken) || exp < currentTimeSeconds) {
                return Result.failed("token无效或已过期");
            }
            redisTemplate.opsForValue().set(key, Strings.EMPTY, (exp - currentTimeSeconds), TimeUnit.SECONDS);
            return Result.success();
        } else {
            consumerTokenServices.revokeToken(token);
            return Result.success();
        }
    }
}
