package com.krrz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.krrz.domain.entity.User;
import org.apache.ibatis.annotations.Update;


/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2022-11-09 19:07:14
 */
public interface UserMapper extends BaseMapper<User> {
    @Update("update sys_user set del_flag =1 where id= #{id}")
    void deleteById(Long id);

    void changeStatus(Long userId);
}
