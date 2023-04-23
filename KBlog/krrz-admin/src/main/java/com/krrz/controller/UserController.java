package com.krrz.controller;

import com.krrz.domain.ResponseResult;
import com.krrz.domain.dto.*;
import com.krrz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PreAuthorize("@ps.hasPermission('system:user:list')")
    @GetMapping("/list")
    public ResponseResult list(@RequestParam("pageNum")Long pageNum, @RequestParam("pageSize")Long pageSize, UserDto userDto){
        return userService.listUser(pageNum,pageSize,userDto);
    }
    @PreAuthorize("@ps.hasPermission('system:user:add')")
    @PostMapping
    public ResponseResult addUser(@RequestBody AddUserDto addUserDto){
        return userService.addUser(addUserDto);
    }
    @PreAuthorize("@ps.hasPermission('system:user:remove')")
    @DeleteMapping("/{id}")
    public ResponseResult deleteById(@PathVariable("id")Long id){
        return userService.deleteById(id);
    }
    @PreAuthorize("@ps.hasPermission('system:user:query')")
    @GetMapping("/{id}")
    public ResponseResult getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }
    @PreAuthorize("@ps.hasPermission('system:user:redit')")
    @PutMapping
    public ResponseResult updateUser(@RequestBody UpdateUserDto addUserDto){
        return userService.updateUser(addUserDto);
    }
    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody UserStatusDto userStatusDto){
        return userService.changeStatus(userStatusDto);
    }
}
