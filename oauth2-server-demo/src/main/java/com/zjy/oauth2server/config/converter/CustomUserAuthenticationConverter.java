package com.zjy.oauth2server.config.converter;

import com.zjy.oauth2server.pojo.entity.oauth2.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 优化自org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter
 * jwt返回的principal改为返回SysUser，增加扩展字段
 *
 * @author: liusongling
 * @date 2020年 07月 10日 13:12
 **/
public class CustomUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
        Map<String, Object> response = new LinkedHashMap<String, Object>();
        response.put(USERNAME, authentication.getName());
        Object principal = authentication.getPrincipal();
        if(principal instanceof User){
            User user = (User) principal;
            if(user != null) {
                response.put("roles", user.getRoles());
                if (user.getId() != null) {
                    response.put("userId", user.getId());
                }
            }
        }
        if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
            response.put(AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
        }
        return response;
    }
}
