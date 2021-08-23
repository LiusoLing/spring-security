//package com.zjy.oauth2server.config.service;
//
//import com.zjy.oauth2server.pojo.entity.system.SysUser;
//import com.zjy.oauth2server.service.system.SysUserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
///**
// * @author liugenlai
// * @since 2021/7/26 10:11
// */
//@Service
//@RequiredArgsConstructor
//public class CustomUserDetailsService extends AbstractUserDetailsService {
//    private final SysUserService sysUserService;
//
//    @Override
//    SysUser findSysUser(String usernameOrMobile) {
//        return sysUserService.findByUsername(usernameOrMobile);
//    }
//}
