package com.zjy.oauth2server.controller;

import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import com.zjy.oauth2server.pojo.constants.SysConstants;
import com.zjy.platform.common.core.result.Result;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.concurrent.TimeUnit;

/**
 * @author liugenlai
 * @since 2021/7/23 17:13
 */
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final RedisTemplate<String, String> redisTemplate;

    @GetMapping("/current")
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

    @GetMapping("/oauth/token_key")
    public Result tokenKey() {


        return null;
    }
}
