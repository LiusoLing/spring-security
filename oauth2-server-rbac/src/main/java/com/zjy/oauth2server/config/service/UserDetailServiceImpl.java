package com.zjy.oauth2server.config.service;

import com.zjy.oauth2server.pojo.entity.SysPermission;
import com.zjy.oauth2server.pojo.entity.SysUser;
import com.zjy.oauth2server.service.SysPermissionService;
import com.zjy.oauth2server.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liugenlai
 * @since 2021/7/21 15:35
 */
@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final SysPermissionService permissionService;
    private final SysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.getByUsername(username);
        if (sysUser == null) {
            throw new UsernameNotFoundException("user can not find");
        }
        List<SysPermission> permissionList = permissionService.selectByUserId(sysUser.getId());
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        permissionList.forEach(permission -> {
            if (permission != null && !StringUtils.isEmpty(permission.getEnname())) {
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(permission.getEnname());
                grantedAuthorities.add(grantedAuthority);
            }
        });
        return new User(sysUser.getUsername(), sysUser.getPassword(), grantedAuthorities);
    }
}
