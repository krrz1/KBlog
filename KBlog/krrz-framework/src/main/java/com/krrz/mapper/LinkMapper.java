package com.krrz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.krrz.domain.entity.Link;
import org.apache.ibatis.annotations.Update;


/**
 * 友链(Link)表数据库访问层
 *
 * @author makejava
 * @since 2022-11-09 18:23:35
 */
public interface LinkMapper extends BaseMapper<Link> {
    @Update("update k_link set del_flag =1 where id=#{id}")
    void deleteById(Long id);
}
