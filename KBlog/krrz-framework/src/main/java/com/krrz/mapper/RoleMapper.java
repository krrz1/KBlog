package com.krrz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.krrz.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2022-11-13 15:09:01
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleKeysByUserId(Long id);

    void updateStatusById(Long id);

    void deleteById(Long id);
}
