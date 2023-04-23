package com.krrz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.krrz.domain.ResponseResult;
import com.krrz.domain.dto.*;
import com.krrz.domain.entity.User;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2022-11-10 15:00:40
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateInfo(User user);

    ResponseResult register(User user);

    ResponseResult listUser(Long pageNum, Long pageSize, UserDto userDto);

    ResponseResult addUser(AddUserDto addUserDto);

    ResponseResult deleteById(Long id);

    ResponseResult getUserById(Long id);

    ResponseResult updateUser(UpdateUserDto addUserDto);

    ResponseResult changeStatus(UserStatusDto userStatusDto);
}
