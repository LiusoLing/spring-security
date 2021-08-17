package com.zjy.oauth2server.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjy.oauth2server.pojo.entity.system.SysPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liugenlai
 * @since 2021/7/26 9:57
 */
@Mapper
public interface SysPermissionMapper extends BaseMapper<SysPermission> {
    /**
     * 查询用户权限列表
     *
     * @param userId
     * @return
     */
    List<SysPermission> selectPermissionByUserId(@Param("userId") Long userId);

}
