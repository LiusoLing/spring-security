package com.zjy.oauth2server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjy.oauth2server.mapper.SysUserMapper;
import com.zjy.oauth2server.pojo.entity.SysUser;
import com.zjy.oauth2server.service.SysUserService;
import org.springframework.stereotype.Service;

/**
 * @author liugenlai
 * @since 2021/7/21 15:18
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public SysUser getByUsername(String username) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysUser::getUsername, username);
        return baseMapper.selectOne(wrapper);
    }
}
