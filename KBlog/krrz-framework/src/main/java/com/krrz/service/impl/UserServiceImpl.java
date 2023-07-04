package com.krrz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krrz.domain.ResponseResult;
import com.krrz.domain.dto.*;
import com.krrz.domain.entity.Role;
import com.krrz.domain.entity.Tag;
import com.krrz.domain.entity.User;
import com.krrz.domain.entity.UserRole;
import com.krrz.domain.vo.PageVo;
import com.krrz.domain.vo.UserInfoVo;
import com.krrz.domain.vo.UserVo;
import com.krrz.domain.vo.UserVoVo;
import com.krrz.enums.AppHttpCodeEnum;
import com.krrz.exception.SystemException;
import com.krrz.service.RoleService;
import com.krrz.service.UserRoleService;
import com.krrz.utils.BeanCopyUtils;
import com.krrz.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.krrz.service.UserService;
import com.krrz.mapper.UserMapper;
import org.springframework.util.StringUtils;

import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2022-11-10 15:00:40
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleService roleService;
    @Override
    public ResponseResult userInfo() {
        //获取当前用户id
        Long userId = SecurityUtils.getUserId();
        //根据id查询用户信息
        User user= getById(userId);
        //封装成userInfoVo
        UserInfoVo vo= BeanCopyUtils.copyBean(user,UserInfoVo.class);
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult updateInfo(User user) {
        updateById(user);
//        update()
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult register(User user) {
        //对数据进行判断  null 或者 空字符串
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAILL_NOT_NULL);
        }
        Matcher matcher = Pattern.compile("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+").matcher(user.getEmail());
        if(!matcher.matches()){
            throw new SystemException(AppHttpCodeEnum.EMAILL_ERROR);
        }
        matcher=Pattern.compile("^(\\w){6,12}$").matcher(user.getPassword());
        if(!matcher.matches()){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_ERROR);
        }
        //对数据是否存在的判duan
        if(userNameExit(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(nickNameExit(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        //对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        user.setAvatar("/static/img/tou.jpg");
        //存入数据库

        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listUser(Long pageNum, Long pageSize, UserDto userDto) {
        //分页查询
        Page<User> page=new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        LambdaQueryWrapper<User> wrapper=new LambdaQueryWrapper<>();

        wrapper.like(StringUtils.hasText(userDto.getUserName()),User::getUserName,userDto.getUserName());
        wrapper.like(StringUtils.hasText(userDto.getPhonenumber()),User::getPhonenumber,userDto.getPhonenumber());
        wrapper.eq(StringUtils.hasText(userDto.getStatus()),User::getStatus,userDto.getStatus());

        page(page, wrapper);
        //封装数据返回
        PageVo pageVo=new PageVo(page.getRecords(),page.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addUser(AddUserDto addUserDto) {
        User user = BeanCopyUtils.copyBean(addUserDto,User.class);
        //新增用户时注意密码加密存储。

    	//用户名不能为空，否则提示：必需填写用户名

    	//用户名必须之前未存在，否则提示：用户名已存在

        //手机号必须之前未存在，否则提示：手机号已存在

    	//邮箱必须之前未存在，否则提示：邮箱已存在
        register(user);
        //修改Role user表
        for(Long roleId:addUserDto.getRoleIds()){
            userRoleService.save(new UserRole(user.getId(),roleId));
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteById(Long id) {
        //逻辑删除
        getBaseMapper().deleteById(id);
        //删除Role_User表的关联
        userRoleService.remove(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId,id));
        //删除
        return ResponseResult.okResult();
    }

    //修改用户时
    @Override
    public ResponseResult getUserById(Long id) {
        //获取用户所关联角色表
        List<UserRole> userRoles = userRoleService.list(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, id));
        List<Long> roleIds=new ArrayList<>();
        for(UserRole userRole:userRoles)
            roleIds.add(userRole.getRoleId());
        //获取所有的角色
        List<Role> roles = roleService.list();
        //查询用户信息
        User user = getById(id);
        UserVoVo userVoVo = BeanCopyUtils.copyBean(user, UserVoVo.class);
        return ResponseResult.okResult(new UserVo(roleIds,roles,userVoVo));
    }

    @Override
    public ResponseResult updateUser(UpdateUserDto addUserDto) {
        User user = BeanCopyUtils.copyBean(addUserDto, User.class);
        //对数据进行判断  null 或者 空字符串
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAILL_NOT_NULL);
        }

        updateById(user);
        //修改User role表
        userRoleService.remove(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId,user.getId()));
        for(Long id:addUserDto.getRoleIds())
            userRoleService.save(new UserRole(user.getId(),id));
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult changeStatus(UserStatusDto userStatusDto) {
        //改变用户状态
        getBaseMapper().changeStatus(userStatusDto.getUserId());
        return ResponseResult.okResult();
    }

    private boolean nickNameExit(String nickName) {
        LambdaQueryWrapper<User> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(User::getNickName,nickName);
        return count(wrapper)>0;
    }

    private boolean userNameExit(String userName) {
        LambdaQueryWrapper<User> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName,userName);
        return count(wrapper)>0;
    }
}

