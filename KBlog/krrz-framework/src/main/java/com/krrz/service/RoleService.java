package com.krrz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.krrz.domain.ResponseResult;
import com.krrz.domain.dto.AddRoleDto;
import com.krrz.domain.dto.RoleStatusDto;
import com.krrz.domain.dto.UpdateRoleDto;
import com.krrz.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2022-11-13 15:08:57
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult listRole(Long pageNum, Long pageSize, String roleName, Long status);

    ResponseResult changeStauts(RoleStatusDto roleStatusDto);

    ResponseResult addRole(AddRoleDto addRoleDto);

    ResponseResult getRoleById(Long id);

    ResponseResult updateRole(UpdateRoleDto updateRoleDto);

    ResponseResult deleteRoleById(Long id);

    List<Role> listAllRole();
}
