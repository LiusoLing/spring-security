package com.zjy.oauth2server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjy.oauth2server.mapper.SysPermissionMapper;
import com.zjy.oauth2server.pojo.entity.SysPermission;
import com.zjy.oauth2server.service.SysPermissionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liugenlai
 * @since 2021/7/21 15:31
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService {

    @Override
    public List<SysPermission> selectByUserId(Long userId) {
        return baseMapper.selectByUserId(userId);
    }
}
