package com.krrz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.krrz.constans.SystemConstants;
import com.krrz.domain.entity.LoginUser;
import com.krrz.domain.entity.User;
import com.krrz.mapper.MenuMapper;
import com.krrz.mapper.UserMapper;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.lang.invoke.LambdaConversionException;
import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuMapper menuMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询信息
        LambdaQueryWrapper<User> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserName,username);
        User user = userMapper.selectOne(lambdaQueryWrapper);
        //判断是否查询道
        if(Objects.isNull(user)){
            throw new RuntimeException("用户不存在");
        }
        //返回用户信息
        //查询权限信息封装
        //如果是后台用户才需要查询权限封装
        if(user.getType().equals(SystemConstants.ADMIN)){
            List<String> list = menuMapper.selectPermsByUserId(user.getId());
            return new LoginUser(user,list);
        }
        return new LoginUser(user,null);
    }
}
