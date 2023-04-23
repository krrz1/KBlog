package com.krrz.controller;

import com.krrz.domain.ResponseResult;
import com.krrz.domain.entity.User;
import com.krrz.enums.AppHttpCodeEnum;
import com.krrz.exception.SystemException;
import com.krrz.service.BlogLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogLoginController {
    @Autowired
    private BlogLoginService blogLoginService;
    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
            //提示必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return blogLoginService.login(user);
    }
    @PostMapping("/logout")
    public ResponseResult logout(){
        return blogLoginService.logout();
    }
}
