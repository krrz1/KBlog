package com.krrz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.krrz.domain.entity.ArticleTag;
import org.apache.ibatis.annotations.Param;

import java.util.Map;


/**
 * 文章标签关联表(ArticleTag)表数据库访问层
 *
 * @author makejava
 * @since 2022-12-16 16:53:57
 */
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {
    void replace(@Param("articleId") Long articleId,@Param("tagId")Long tagId);
}
