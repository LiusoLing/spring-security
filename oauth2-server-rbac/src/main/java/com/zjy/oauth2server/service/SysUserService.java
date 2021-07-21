package com.zjy.oauth2server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjy.oauth2server.pojo.entity.SysUser;

/**
 * @author liugenlai
 * @since 2021/7/21 15:14
 */
public interface SysUserService extends IService<SysUser> {

    SysUser getByUsername(String username);
}
