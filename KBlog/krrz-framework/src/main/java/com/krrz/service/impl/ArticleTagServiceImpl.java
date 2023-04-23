package com.krrz.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krrz.domain.entity.ArticleTag;
import com.krrz.mapper.ArticleTagMapper;
import com.krrz.service.ArticleTagService;
import org.springframework.stereotype.Service;

/**
 * 文章标签关联表(ArticleTag)表服务实现类
 *
 * @author makejava
 * @since 2022-12-16 16:53:57
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

    @Override
    public void replace(Long articleId, Long tagId) {
        getBaseMapper().replace(articleId,tagId);
    }

    @Override
    public void deleteTagByArticleId(Long articleId) {
        getBaseMapper().delete(new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getArticleId,articleId));
    }
}

