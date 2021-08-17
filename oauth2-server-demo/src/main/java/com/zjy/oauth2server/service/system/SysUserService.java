package com.zjy.oauth2server.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjy.oauth2server.pojo.entity.system.SysUser;

/**
 * @author liugenlai
 * @since 2021/7/26 9:57
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 通过用户名查询
     *
     * @param username 用户名
     * @return 用户信息
     */
    SysUser findByUsername(String username);

    /**
     * 通过手机号查询
     *
     * @param mobile 手机号
     * @return 用户信息
     */
    SysUser findByMobile(String mobile);
}
