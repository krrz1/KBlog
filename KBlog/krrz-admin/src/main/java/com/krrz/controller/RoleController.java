package com.krrz.controller;

import com.krrz.domain.ResponseResult;
import com.krrz.domain.dto.AddRoleDto;
import com.krrz.domain.dto.RoleStatusDto;
import com.krrz.domain.dto.UpdateRoleDto;
import com.krrz.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/role")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @PreAuthorize("@ps.hasPermission('system:role:list')")
    @RequestMapping("/list")
    public ResponseResult list(@RequestParam(required = false,name = "roleName")String roleName,@RequestParam(required = false,name = "status")Long status,@RequestParam("pageNum")Long pageNum,@RequestParam("pageSize")Long pageSize){
        return roleService.listRole(pageNum,pageSize,roleName,status);
    }
    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody RoleStatusDto roleStatusDto){
        return roleService.changeStauts(roleStatusDto);
    }
    @PreAuthorize("@ps.hasPermission('system:role:add')")
    @PostMapping
    public ResponseResult addRole(@RequestBody AddRoleDto addRoleDto){
        return roleService.addRole(addRoleDto);
    }
    @PreAuthorize("@ps.hasPermission('system:role:query')")
    @GetMapping("/{id}")
    public ResponseResult getRoleById(@PathVariable("id")Long id){
        return roleService.getRoleById(id);
    }
    @PreAuthorize("@ps.hasPermission('system:role:edit')")
    @PutMapping
    public ResponseResult updateRole(@RequestBody UpdateRoleDto updateRoleDto){
        return roleService.updateRole(updateRoleDto);
    }
    @PreAuthorize("@ps.hasPermission('system:role:remove')")
    @DeleteMapping("/{id}")
    public ResponseResult deleteRole(@PathVariable("id")Long id){
        return roleService.deleteRoleById(id);
    }
    //新增用户时
    @GetMapping("/listAllRole")
    public ResponseResult listAllRole(){
        return ResponseResult.okResult(roleService.listAllRole());
    }
}
