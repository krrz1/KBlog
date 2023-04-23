package com.krrz.controller;

import com.krrz.domain.ResponseResult;
import com.krrz.domain.entity.User;
import com.krrz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/userInfo")
    public ResponseResult userInfo(){
        return userService.userInfo();
    }
    @PutMapping("/userInfo")
    public ResponseResult updateInfo(@RequestBody User user){
        return userService.updateInfo(user);
    }
    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user){
        return userService.register(user);
    }
}
