package com.zjy.oauth2server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjy.oauth2server.pojo.entity.SysPermission;

import java.util.List;

/**
 * @author liugenlai
 * @since 2021/7/21 15:30
 */
public interface SysPermissionService extends IService<SysPermission> {

    List<SysPermission> selectByUserId(Long userId);
}
