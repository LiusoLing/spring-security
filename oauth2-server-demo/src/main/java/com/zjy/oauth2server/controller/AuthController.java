package com.zjy.oauth2server.controller;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import com.zjy.oauth2server.pojo.constants.SysConstants;
import com.zjy.platform.common.core.result.Result;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.KeyPair;
import java.security.Principal;
import java.security.interfaces.RSAPublicKey;
import java.util.concurrent.TimeUnit;

/**
 * @author liugenlai
 * @since 2021/7/23 17:13
 */
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final RedisTemplate<String, String> redisTemplate;
    private final KeyPair keyPair;

    @GetMapping("/current")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    public Result userInfo(Principal principal) {
        return Result.success(principal);
    }

    @GetMapping("/captcha")
    @ApiOperation("获取验证码")
    public void captcha(HttpServletResponse response) throws Exception {
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
        redisTemplate.opsForValue().set(String.format(SysConstants.CAPTCHA_REDIS_KEY, null), specCaptcha.text(), 2, TimeUnit.MINUTES);
        // 输出图片流
        specCaptcha.out(response.getOutputStream());
    }

    @GetMapping("/.well-known/jwks.json")
    public Result jwksJson() {
        RSAPublicKey publicKey = (RSAPublicKey) this.keyPair.getPublic();
        RSAKey key = new RSAKey.Builder(publicKey).build();
        return Result.success(new JWKSet(key).toJSONObject());
    }

    @GetMapping("/oauth/token_key")
    public Result tokenKey() throws IOException {
        ClassPathResource resource = new ClassPathResource("public.txt");
        String publicKey = IOUtils.toString(resource.getInputStream(), "UTF-8");
        return Result.success(publicKey);
    }

    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("qwer"));
        System.out.println(new BCryptPasswordEncoder().matches("123456",
                "$2a$10$N.k6Q38FB0tlqyb3Af..se98ENWUnl3OB6b0RNtj8C6KH75Jo5V6a"));
    }
}
