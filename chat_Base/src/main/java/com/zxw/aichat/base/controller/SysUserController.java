package com.zxw.aichat.base.controller;

import com.zxw.aichat.base.annotation.ORpose;
import com.zxw.aichat.base.entity.SysUser;
import com.zxw.aichat.base.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@ORpose
@Api(tags = "用户接口")
@RestController
@RequestMapping("/base/users")
public class SysUserController {

    private final SysUserService sysUserService;

    public SysUserController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @ApiOperation("查询用户列表")
    @GetMapping
    public List<SysUser> list(
            @ApiParam(value = "登录用户名，支持模糊查询")
            @RequestParam(required = false) String username) {
        return sysUserService.listUsers(username);
    }

    @ApiOperation("查询用户详情")
    @GetMapping("/{id}")
    public SysUser getById(
            @ApiParam(value = "用户ID", required = true)
            @PathVariable Long id) {
        return sysUserService.getById(id);
    }

    @ApiOperation("新增用户")
    @PostMapping
    public Long create(
            @ApiParam(value = "用户信息", required = true)
            @RequestBody SysUser user) {
        return sysUserService.createUser(user);
    }

    @ApiOperation("更新用户")
    @PutMapping("/{id}")
    public Boolean update(
            @ApiParam(value = "用户ID", required = true)
            @PathVariable Long id,
            @ApiParam(value = "用户信息", required = true)
            @RequestBody SysUser user) {
        return sysUserService.updateUser(id, user);
    }

    @ApiOperation("删除用户")
    @DeleteMapping("/{id}")
    public Boolean delete(
            @ApiParam(value = "用户ID", required = true)
            @PathVariable Long id) {
        return sysUserService.deleteUser(id);
    }
}
