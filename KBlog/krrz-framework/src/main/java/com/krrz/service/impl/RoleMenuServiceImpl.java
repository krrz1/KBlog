package com.krrz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krrz.domain.entity.RoleMenu;
import com.krrz.mapper.RoleMenuMapper;
import com.krrz.service.RoleMenuService;
import org.springframework.stereotype.Service;

/**
 * 角色和菜单关联表(RoleMenu)表服务实现类
 *
 * @author makejava
 * @since 2023-02-08 04:46:07
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

}

