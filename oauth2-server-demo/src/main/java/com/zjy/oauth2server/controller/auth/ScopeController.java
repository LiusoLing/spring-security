package com.zjy.oauth2server.controller.auth;

import com.zjy.oauth2server.pojo.entity.oauth2.Scope;
import com.zjy.oauth2server.service.oauth2.ScopeService;
import com.zjy.platform.common.core.result.Result;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author liugenlai
 * @since 2021/8/18 8:40
 */
@RestController
@RequiredArgsConstructor
public class ScopeController {
    private final ScopeService scopeService;
    
    /**
     * 获取OAuth2客户端授权范围列表
     */
    @GetMapping(value = "/list")
    @ApiOperation("获取OAuth2客户端授权范围列表")
    public Result<List<Scope>> list() {
        return Result.success(scopeService.list());
    }

    /**
     * 新增OAuth2客户端授权范围
     */
    @PostMapping(value = "/add")
    @ApiOperation("新增OAuth2客户端授权范围")
    public Result<String> add(Scope scope) {
        scopeService.save(scope);
        return Result.success();
    }

    /**
     * 删除OAuth2客户端授权范围
     */
    @PostMapping(value = "/delete/{id}")
    @ApiOperation("删除OAuth2客户端授权范围")
    public Result<String> delete(@PathVariable("id") Long id) {
        scopeService.removeById(id);
        return Result.success();
    }

    /**
     * 修改OAuth2客户端授权范围
     */
    @PostMapping(value = "/update")
    @ApiOperation("修改OAuth2客户端授权范围")
    public Result<String> update(Scope scope) {
        scopeService.updateById(scope);
        return Result.success();
    }

    /**
     * 获取OAuth2客户端授权范围
     */
    @GetMapping(value = "/get/{id}")
    @ApiOperation("获取OAuth2客户端授权范围")
    public Result<Scope> get(@PathVariable("id") Long id) {
        return Result.success(scopeService.getById(id));
    }
}
