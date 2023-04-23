package com.krrz.service.impl;

import com.krrz.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ps")
public class PermissionServiceImpl {
    /*
     *@Description:判断当前用户是否具有permission
     *@Params:权限名
     *@Return:
     *@Author:krrz
     *@Date:2023/2/6
     */
    public boolean hasPermission(String permission){
        //如果是超级管理员 直接返回true
        if(SecurityUtils.isAdmin()) return true;
        //否则获取当前用户权限列表
        List<String> permissions = SecurityUtils.getLoginUser().getPermissions();
        if(permission==null) return false;
        return permissions.contains(permission);
    }
}
