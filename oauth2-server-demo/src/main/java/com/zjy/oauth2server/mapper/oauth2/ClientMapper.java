package com.zjy.oauth2server.mapper.oauth2;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjy.oauth2server.pojo.entity.oauth2.Client;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author liusongling
 * @since 2021-08-17 22:58:09
 */
@Mapper
public interface ClientMapper extends BaseMapper<Client> {
}
