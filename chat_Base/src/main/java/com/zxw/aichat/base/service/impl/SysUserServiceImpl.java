package com.zxw.aichat.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxw.aichat.base.entity.SysUser;
import com.zxw.aichat.base.mapper.SysUserMapper;
import com.zxw.aichat.base.service.SysUserService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public List<SysUser> listUsers(String username) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<SysUser>()
                .like(StringUtils.hasText(username), SysUser::getUsername, username)
                .orderByDesc(SysUser::getCreatedTime);
        return list(wrapper);
    }

    @Override
    public Long createUser(SysUser user) {
        save(user);
        return user.getId();
    }

    @Override
    public boolean updateUser(Long id, SysUser user) {
        user.setId(id);
        return updateById(user);
    }

    @Override
    public boolean deleteUser(Long id) {
        return removeById(id);
    }
}
