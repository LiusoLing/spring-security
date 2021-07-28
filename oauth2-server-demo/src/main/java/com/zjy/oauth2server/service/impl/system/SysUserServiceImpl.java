package com.zjy.oauth2server.service.impl.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjy.oauth2server.mapper.system.SysUserMapper;
import com.zjy.oauth2server.pojo.entity.SysUser;
import com.zjy.oauth2server.service.system.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author liugenlai
 * @since 2021/7/26 9:57
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    /**
     * 通过手机号查询
     *
     * @param mobile 手机号
     * @return 用户信息
     */
    @Override
    public SysUser findByMobile(String mobile) {
        return null;
    }

    /**
     * 通过用户名查询
     *
     * @param username 用户名
     * @return 用户信息
     */
    @Override
    public SysUser findByUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", username);
        return baseMapper.selectOne(queryWrapper);
    }
}
