package com.zjy.oauth2server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjy.oauth2server.pojo.entity.SysPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liugenlai
 * @since 2021/7/21 15:29
 */
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    List<SysPermission> selectByUserId(@Param("userId") Long userId);
}
