package com.krrz.service;

import com.krrz.domain.ResponseResult;
import com.krrz.domain.entity.User;

public interface AdminLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
