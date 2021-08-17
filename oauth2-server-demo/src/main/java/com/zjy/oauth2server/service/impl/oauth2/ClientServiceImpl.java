package com.zjy.oauth2server.service.impl.oauth2;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjy.oauth2server.mapper.oauth2.ClientMapper;
import com.zjy.oauth2server.pojo.entity.oauth2.Client;
import com.zjy.oauth2server.service.oauth2.ClientService;
import org.springframework.stereotype.Service;

/**
 * @author liusongling
 * @since 2021-08-17 22:56:00
 */
@Service
public class ClientServiceImpl extends ServiceImpl<ClientMapper, Client> implements ClientService {

}
