package com.zjy.oauth2server.service.impl.oauth2;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjy.oauth2server.mapper.oauth2.ScopeMapper;
import com.zjy.oauth2server.pojo.entity.oauth2.Scope;
import com.zjy.oauth2server.service.oauth2.ScopeService;
import org.springframework.stereotype.Service;

/**
 * @author liusongling
 * @since 2021-08-17 22:57:00
 */
@Service
public class ScopeServiceImpl extends ServiceImpl<ScopeMapper, Scope> implements ScopeService {
}
