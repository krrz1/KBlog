package com.krrz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.krrz.domain.ResponseResult;
import com.krrz.domain.dto.AddArticleDto;
import com.krrz.domain.dto.ArticleDetailAdminDto;
import com.krrz.domain.dto.ListArticleDto;
import com.krrz.domain.entity.Article;
import com.krrz.domain.vo.ArticleDetailAdminVo;

public interface ArticleService2 extends IService<Article> {
    //查询热门文章封装成
    ResponseResult hostArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult add(AddArticleDto articleDto);

    ResponseResult listArticle(ListArticleDto listArticleDto);

    ArticleDetailAdminVo queryArticleById(Long id);

    void updateArticle(ArticleDetailAdminDto articleDetailAdminDto);

    void deleteById(Long id);
}
