package com.krrz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.krrz.domain.entity.Category;
import org.apache.ibatis.annotations.Update;


/**
 * 分类表(Category)表数据库访问层
 *
 * @author makejava
 * @since 2022-11-05 17:20:09
 */
public interface CategoryMapper extends BaseMapper<Category> {
    @Update("update k_category set del_flag = 1 where id =#{id}")
    void deleteById(Long id);
}
