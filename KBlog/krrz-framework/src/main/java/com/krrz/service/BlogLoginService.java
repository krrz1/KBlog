package com.krrz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.krrz.domain.ResponseResult;
import com.krrz.domain.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface BlogLoginService{

    ResponseResult login(User user);

    ResponseResult logout();
}
