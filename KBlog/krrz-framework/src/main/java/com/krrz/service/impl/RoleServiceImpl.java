package com.krrz.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krrz.domain.ResponseResult;
import com.krrz.domain.dto.AddRoleDto;
import com.krrz.domain.dto.RoleStatusDto;
import com.krrz.domain.dto.UpdateRoleDto;
import com.krrz.domain.entity.Menu;
import com.krrz.domain.entity.Role;
import com.krrz.domain.entity.RoleMenu;
import com.krrz.domain.entity.Tag;
import com.krrz.domain.vo.MenuTreeVo;
import com.krrz.domain.vo.PageVo;
import com.krrz.domain.vo.RoleVo;
import com.krrz.mapper.RoleMapper;
import com.krrz.service.MenuService;
import com.krrz.service.RoleMenuService;
import com.krrz.service.RoleService;
import com.krrz.utils.BeanCopyUtils;
import com.krrz.utils.IsAdminUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2022-11-13 15:08:57
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    private MenuServiceImpl menuService;
    @Autowired
    private RoleMenuService roleMenuService;
    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否管理院  如果是 只需返回admin
        if(IsAdminUtils.isAdmin(id)){
            List<String > roleKeys=new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        //否则查询用户具有的角色
        return getBaseMapper().selectRoleKeysByUserId(id);
    }

    @Override
    public ResponseResult listRole(Long pageNum, Long pageSize, String roleName, Long status) {
        //分页查询
        Page<Role> page=new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        LambdaQueryWrapper<Role> wrapper=new LambdaQueryWrapper<>();

        wrapper.eq(StringUtils.hasText(roleName),Role::getRoleName,roleName);
        wrapper.eq(status!=null,Role::getStatus,status);

        page(page, wrapper);
        //封装数据返回
        PageVo pageVo=new PageVo(page.getRecords(),page.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult changeStauts(RoleStatusDto roleStatusDto) {
        getBaseMapper().updateStatusById(roleStatusDto.getRoleId());
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addRole(AddRoleDto addRoleDto) {
        Role role = BeanCopyUtils.copyBean(addRoleDto, Role.class);
        save(role);
        //增加role和menu的关联
        for(Long menuId:addRoleDto.getMenuIds()){
            roleMenuService.save(new RoleMenu(role.getId(),menuId));
        }

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getRoleById(Long id) {
        Role role = getById(id);
        RoleVo roleVo = BeanCopyUtils.copyBean(role, RoleVo.class);
        return ResponseResult.okResult(roleVo);
    }

    @Override
    public ResponseResult updateRole(UpdateRoleDto updateRoleDto) {
        //先把信息修改在Role表
        Role role = BeanCopyUtils.copyBean(updateRoleDto, Role.class);
        updateById(role);
        //再修改关联表Menu Role的表数据
        //先删再加
        roleMenuService.remove(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId,updateRoleDto.getId()));
        for(Long menuId:updateRoleDto.getMenuIds()){
            roleMenuService.save(new RoleMenu(updateRoleDto.getId(),menuId));
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteRoleById(Long id) {
        //逻辑删除Role表
        getBaseMapper().deleteById(id);
        //删除Role——menu关联表
        roleMenuService.remove(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId,id));
        return ResponseResult.okResult();
    }

    @Override
    public List<Role> listAllRole() {
        return list();
    }
}

