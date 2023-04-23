package com.krrz.service.impl;

import com.krrz.config.JwtUtil;
import com.krrz.config.RedisCache;
import com.krrz.domain.ResponseResult;
import com.krrz.domain.entity.LoginUser;
import com.krrz.domain.entity.User;
import com.krrz.domain.vo.BlogUserLoginVo;
import com.krrz.domain.vo.UserInfoVo;
import com.krrz.service.BlogLoginService;
import com.krrz.utils.BeanCopyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.management.RuntimeMBeanException;
import java.util.Objects;
@Service
public class BlogLoginServiceImpl implements BlogLoginService {
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
        redisCache.setCacheObject("bloglogin:"+userId,loginUser);
        //把token和userInfo封
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser, UserInfoVo.class);
        BlogUserLoginVo blogUserLoginVo=new BlogUserLoginVo(jwt,userInfoVo);
        return ResponseResult.okResult(blogUserLoginVo);
    }

    @Override
    public ResponseResult logout() {
        //获取token 解析
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser= (LoginUser) authentication.getPrincipal();
        //获取userId
        Long userId = loginUser.getUser().getId();
        //删除redis的信息
        redisCache.deleteObject("bloglogin:"+userId);
        return ResponseResult.okResult();
    }
}
