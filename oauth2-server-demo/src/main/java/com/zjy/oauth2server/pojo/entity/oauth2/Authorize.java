package com.zjy.oauth2server.pojo.entity.oauth2;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 抽取出通用的角色和权限信息，交由各个系统自己去查询
 *
 * @author liugenlai
 * @since 2021/8/23 15:28
 */
public class Authorize {
    /**
     * 权限或资源列表
     */
    private Collection<String> resources = new ArrayList<>();
    /**
     * 角色列表
     */
    private Collection<String> roles = new ArrayList<>();

    public Collection<String> getResources() {
        return resources;
    }

    public void setResources(Collection<String> resources) {
        this.resources = resources;
    }

    public Collection<String> getRoles() {
        return roles;
    }

    public void setRoles(Collection<String> roles) {
        this.roles = roles;
    }
}
