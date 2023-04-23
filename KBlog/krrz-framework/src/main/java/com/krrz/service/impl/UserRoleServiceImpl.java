package com.krrz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krrz.domain.entity.UserRole;
import com.krrz.mapper.UserRoleMapper;
import com.krrz.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户和角色关联表(UserRole)表服务实现类
 *
 * @author makejava
 * @since 2023-02-08 16:45:06
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}

