package com.krrz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.krrz.domain.entity.ArticleTag;


/**
 * 文章标签关联表(ArticleTag)表服务接口
 *
 * @author makejava
 * @since 2022-12-16 16:53:57
 */
public interface ArticleTagService extends IService<ArticleTag> {
    void replace(Long articleId,Long tagId);
    void deleteTagByArticleId(Long articleId);
}
