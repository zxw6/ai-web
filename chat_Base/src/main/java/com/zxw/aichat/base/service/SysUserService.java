package com.zxw.aichat.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxw.aichat.base.entity.SysUser;
import java.util.List;

public interface SysUserService extends IService<SysUser> {

    List<SysUser> listUsers(String username);

    Long createUser(SysUser user);

    boolean updateUser(Long id, SysUser user);

    boolean deleteUser(Long id);
}
