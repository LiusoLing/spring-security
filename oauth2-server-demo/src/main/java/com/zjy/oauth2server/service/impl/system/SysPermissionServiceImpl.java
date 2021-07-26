package com.zjy.oauth2server.service.impl.system;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjy.oauth2server.mapper.system.SysPermissionMapper;
import com.zjy.oauth2server.pojo.entity.SysPermission;
import com.zjy.oauth2server.service.system.SysPermissionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liugenlai
 * @since 2021/7/26 9:57
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService {

    /**
     * 通过用户id查询所拥有权限
     * @param userId
     * @return
     */
    @Override
    public List<SysPermission> findByUserId(Long userId) {
        if (userId == null) {
            return null;
        }
        List<SysPermission> permissionList = baseMapper.selectPermissionByUserId(userId);
        // 如果没有权限，则将集合中的数据null移除
//        permissionList.remove(null);
        return permissionList;
    }

}