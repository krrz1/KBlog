package com.krrz.service.impl;

import com.krrz.config.JwtUtil;
import com.krrz.config.RedisCache;
import com.krrz.domain.ResponseResult;
import com.krrz.domain.entity.LoginUser;
import com.krrz.domain.entity.User;
import com.krrz.domain.vo.BlogUserLoginVo;
import com.krrz.domain.vo.UserInfoVo;
import com.krrz.service.AdminLoginService;
import com.krrz.utils.BeanCopyUtils;
import com.krrz.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class AdminLoginServiceImpl implements AdminLoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;
    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //是否认证通过
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        //获取userId生成token
        LoginUser loginUser= (LoginUser) authenticate.getPrincipal();
        String userId= loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        //把用户信息存入redis
        redisCache.setCacheObject("login:"+userId,loginUser);
        //把token封奘
        Map<String ,String> map=new HashMap<>();
        map.put("token",jwt);
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult logout() {
        //获取当前登录用户id
        Long userId = SecurityUtils.getUserId();
        //删除redis中对应的
        redisCache.deleteObject("login:"+userId);
        return ResponseResult.okResult();
    }
}
