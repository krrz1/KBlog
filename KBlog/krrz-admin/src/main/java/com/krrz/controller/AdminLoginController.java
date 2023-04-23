package com.krrz.controller;

import com.krrz.config.RedisCache;
import com.krrz.domain.ResponseResult;
import com.krrz.domain.entity.LoginUser;
import com.krrz.domain.entity.Menu;
import com.krrz.domain.entity.User;
import com.krrz.domain.vo.AdminUserInfoVo;
import com.krrz.domain.vo.RoutersVo;
import com.krrz.domain.vo.UserInfoVo;
import com.krrz.enums.AppHttpCodeEnum;
import com.krrz.exception.SystemException;
import com.krrz.service.AdminLoginService;
import com.krrz.service.MenuService;
import com.krrz.service.RoleService;
import com.krrz.utils.BeanCopyUtils;
import com.krrz.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdminLoginController {
    @Autowired
    private AdminLoginService adminLoginService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RedisCache redisCache;
    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
            //提示必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return adminLoginService.login(user);
    }
    @GetMapping("/getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo(){
        //获取当前登录的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //根据用户id查询权限信息
        List<String> perms=menuService.selectPermsByUserId(loginUser.getUser().getId());
        //根据用户id查询角色信息
        List<String> roleKeys=roleService.selectRoleKeyByUserId(loginUser.getUser().getId());
        //封装数据返回
        UserInfoVo user = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        AdminUserInfoVo adminUserInfoVo=new AdminUserInfoVo(perms,roleKeys,user);

        return ResponseResult.okResult(adminUserInfoVo);
    }
    @GetMapping("/getRouters")
    public ResponseResult<RoutersVo> getRouters(){
        Long userId = SecurityUtils.getUserId();
        //查询menu 结果是tree形式
        List<Menu> menus=menuService.selectRouterMenuTreeByUserId(userId);
        //封装数据返回
        return ResponseResult.okResult(new RoutersVo(menus));
    }
    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return adminLoginService.logout();
    }
}
