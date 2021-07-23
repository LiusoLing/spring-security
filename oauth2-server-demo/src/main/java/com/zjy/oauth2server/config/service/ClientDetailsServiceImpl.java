package com.zjy.oauth2server.config.service;

import org.springframework.security.oauth2.provider.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liugenlai
 * @since 2021/7/23 16:40
 */
@Service
public class ClientDetailsServiceImpl implements ClientDetailsService, ClientRegistrationService {

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return null;
    }

    @Override
    public void addClientDetails(ClientDetails clientDetails) throws ClientAlreadyExistsException {

    }

    @Override
    public void updateClientDetails(ClientDetails clientDetails) throws NoSuchClientException {

    }

    @Override
    public void updateClientSecret(String clientId, String secret) throws NoSuchClientException {

    }

    @Override
    public void removeClientDetails(String clientId) throws NoSuchClientException {

    }

    @Override
    public List<ClientDetails> listClientDetails() {
        return null;
    }
}
