package com.zjy.oauth2server.config;

import com.zjy.oauth2server.config.service.CustomUserDetailsService;
import com.zjy.oauth2server.exception.CustomAccessDeniedHandler;
import com.zjy.oauth2server.exception.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 安全配置类
 *
 * @author liugenlai
 * @since 2021/7/23 15:19
 */
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomUserDetailsService userDetailsService;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    /**
     * 配置用户信息，数据库方式/内存方式
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    /**
     * 资源权限配置，可以配置以下项：
     * 1、被拦截的资源
     * 2、资源对应的角色权限
     * 3、定制认证方式httpBasic()、httpForm
     * 4、定制登陆页面、登陆请求地址、错误处理方式
     * 5、自定义SpringSecurity过滤器
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                // 认证服务器相关资源全部放行，用于处理认证
                .authorizeRequests()
                .antMatchers("/oauth/**").permitAll()
                //.antMatchers("/api/**").authenticated()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                .formLogin().disable()
                // 注销处理
                .logout()
                .and()
                // IF_REQUIRED 可以支持所有的oauth2认证方式，STATELESS支持 密码、客户端等模式
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        // 自定义退出时处理程序
        //.logoutSuccessHandler(new OauthLogoutSuccessHandler())
        // 退出清楚缓存
        //.addLogoutHandler(oauthLogoutHandler)
        // 指定是否在注销用户时清除，默认为false
        ;
    }


    /**
     * 密码模式需要这个认证管理器
     * <p>这一步的配置是必不可少的，否则SpringBoot会自动配置一个AuthenticationManager,覆盖掉内存中的用户</p>
     *
     * @return 认证管理对象
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * http.permitAll 方法不会绕开spring security的过滤器验证，相当于只是允许该路径通过过滤器
     * web.ignoring 是直接绕开spring security的所有filter，直接跳过验证
     * /captcha 验证码接口
     *
     * @param web
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/resources/**", "/static/**", "/public/**",
                "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/captcha", "/.well-known/jwks.json");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
