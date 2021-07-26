package com.zjy.oauth2server.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjy.oauth2server.pojo.entity.SysPermission;

import java.util.List;

/**
 * @author liugenlai
 * @since 2021/7/26 9:57
 */
public interface SysPermissionService extends IService<SysPermission> {

    /**
     * 通过用户id查询所拥有权限
     * @param userId
     * @return
     */
    List<SysPermission> findByUserId(Long userId);

}