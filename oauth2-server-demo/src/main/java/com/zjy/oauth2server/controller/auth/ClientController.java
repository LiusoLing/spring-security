package com.zjy.oauth2server.controller.auth;

import com.zjy.oauth2server.pojo.entity.oauth2.Client;
import com.zjy.oauth2server.service.oauth2.ClientService;
import com.zjy.platform.common.core.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liugenlai
 * @since 2021/8/18 8:40
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth/client")
@Api(tags = "OAuth2客户端管理接口")
public class ClientController {
    private final ClientService clientService;

    /**
     * 获取OAuth2客户端列表
     */
    @PreAuthorize("hasRole('admin')")
    @GetMapping(value = "/list")
    @ApiOperation("获取OAuth2客户端列表")
    public Result<List<Client>> list() {
        return Result.success(clientService.list());
    }

    /**
     * 新增OAuth2客户端
     */
    @PreAuthorize("hasRole('admin')")
    @PostMapping(value = "/add")
    @ApiOperation("新增OAuth2客户端")
    public Result<String> add(@RequestBody Client client) {
        clientService.save(client);
        return Result.success();
    }

    /**
     * 删除OAuth2客户端
     */
    @PreAuthorize("hasRole('admin')")
    @PostMapping(value = "/delete/{clientId}")
    @ApiOperation("删除OAuth2客户端")
    public Result<String> delete(@PathVariable("clientId") String clientId) {
        clientService.removeById(clientId);
        return Result.success();
    }

    /**
     * 修改OAuth2客户端
     */
    @PreAuthorize("hasRole('admin')")
    @PostMapping(value = "/update")
    @ApiOperation("修改OAuth2客户端")
    public Result<String> update(@RequestBody Client client) {
        clientService.updateById(client);
        return Result.success();
    }

    /**
     * 获取OAuth2客户端
     */
    @PreAuthorize("hasRole('admin')")
    @GetMapping(value = "/get/{clientId}")
    @ApiOperation("OAuth2客户端详情")
    public Result<Client> get(@PathVariable("clientId") String clientId) {
        return Result.success(clientService.getById(clientId));
    }
}
