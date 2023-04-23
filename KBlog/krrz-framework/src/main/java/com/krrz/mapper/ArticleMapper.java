package com.krrz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.krrz.domain.entity.Article;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface ArticleMapper extends BaseMapper<Article> {
    @Update("update k_article set del_flag = 1 where id = #{id}")
    void deleteById(@Param("id") Long id);
}
