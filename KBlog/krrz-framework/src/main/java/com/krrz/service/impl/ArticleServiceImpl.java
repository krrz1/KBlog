package com.krrz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krrz.config.RedisCache;
import com.krrz.constans.SystemConstants;
import com.krrz.domain.ResponseResult;
import com.krrz.domain.dto.AddArticleDto;
import com.krrz.domain.dto.ArticleDetailAdminDto;
import com.krrz.domain.dto.ListArticleDto;
import com.krrz.domain.entity.Article;
import com.krrz.domain.entity.ArticleTag;
import com.krrz.domain.entity.Category;
import com.krrz.domain.vo.*;
import com.krrz.mapper.ArticleMapper;
import com.krrz.mapper.ArticleTagMapper;
import com.krrz.service.ArticleService;
import com.krrz.service.ArticleTagService;
import com.krrz.service.CategoryService;
import com.krrz.service.CommentService;
import com.krrz.utils.BeanCopyUtils;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Autowired
    CategoryService categoryService;
    @Autowired
    RedisCache redisCache;
    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private CommentService commentService;
    @Override
    public ResponseResult hostArticleList() {
        //查询热门文章封装成
        LambdaQueryWrapper<Article> wrapper=new LambdaQueryWrapper<>();
        //封装查询条件
        //正式文章
        wrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //按照浏览量排序
        wrapper.orderByDesc(Article::getViewCount);
        //最多只查询10跳
        Page<Article> page=new Page<>(1,10);
        page(page,wrapper);
        List<Article> articles=page.getRecords();
        //bean 拷贝
        List<HotArticleVo> articleVos = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);

        return ResponseResult.okResult(articleVos);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //查询条件
        LambdaQueryWrapper<Article> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        //如果 有categoryId就要求查询时要和传入时相同
        lambdaQueryWrapper.eq(Objects.nonNull(categoryId) && categoryId>0,Article::getCategoryId,categoryId);
        //状态是正式发布的
        lambdaQueryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        //istop进行降序
        lambdaQueryWrapper.orderByDesc(Article::getIsTop);

        //分页查询
        Page<Article> page=new Page<>(pageNum,pageSize);
        page(page,lambdaQueryWrapper);
        //查询categoryName
        List<Article> articles=page.getRecords();
//        for (Article article : articles) {
//            Category category=categoryService.getById(article.getCategoryId());
//            article.setCategoryName(category.getName());
//        }
//       articles.stream()
//                .map(new Function<Article, Article>() {
//                    @Override
//                    public Article apply(Article article) {
//                        //获取分类id
//                        Category category = categoryService.getById(article.getCategoryId());
//                        article.setCategoryName(category.getName());
//                        return article;
//                    }
//                }).collect(Collectors.toList());
       articles.stream()
               .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName())).collect(Collectors.toList());
        //封装查询结果                                                   数据地址一样
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);


        PageVo pageVo = new PageVo(articleListVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id查询文章
        Article article = getById(id);
        //从redis获得viewCount
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        article.setViewCount(viewCount.longValue());
        //转换为VO
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //根据分类id查询分类名
        Long categoryId = articleDetailVo.getCategoryId();
        Category category=categoryService.getById(categoryId);
        if(category!=null){
            articleDetailVo.setCategoryName(category.getName());
        }
        //封装响应返回
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        //更新redis中对应id的浏览量
        redisCache.incrementCacheMapValue("article:viewCount",id.toString(),1);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult add(AddArticleDto articleDto) {
        //添加博客
        Article article=BeanCopyUtils.copyBean(articleDto,Article.class);
        save(article);
        //新建博客和标签的关联
        List<ArticleTag> articleTags=articleDto.getTags().stream()
                .map(tagId->new ArticleTag(article.getId(),tagId)).collect(Collectors.toList());
        //更新redis 访问量 0
        List<Article> articles = getBaseMapper().selectList(null);
        Map<String , Integer> ViewCountMap = articles.stream()
                .collect(Collectors.toMap(article1 -> article1.getId().toString(), article1 -> article1.getViewCount().intValue()));
        //存储到redis
        redisCache.setCacheMap("article:viewCount",ViewCountMap);
        //将新关联项存入数据库
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listArticle(ListArticleDto listArticleDto) {
        LambdaQueryWrapper<Article> wrapper=new LambdaQueryWrapper<>();
        if(!listArticleDto.getTitle().equals(""))
        wrapper.like(Article::getTitle,listArticleDto.getTitle());
        if(!listArticleDto.getSummary().equals(""))
        wrapper.like(Article::getSummary,listArticleDto.getSummary());
        //分页查询
        Page<Article> page = new Page(listArticleDto.getPageNum(),listArticleDto.getPageSize());
        page(page,wrapper);

        //List<Article> articles = toCommentVoList(page.getRecords());
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);
        return ResponseResult.okResult(new PageVo(articleListVos,page.getTotal()));
    }

    @Override
    public ArticleDetailAdminVo queryArticleById(Long id) {
        //根据id查询文章
        Article article=getById(id);
        //从redis获得viewCount
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        article.setViewCount(viewCount.longValue());
        //转换为VO
        ArticleDetailAdminVo articleDetaiAdminVo = BeanCopyUtils.copyBean(article, ArticleDetailAdminVo.class);
        //查询tags
        LambdaQueryWrapper<ArticleTag> wrapper=new LambdaQueryWrapper();
        wrapper.eq(ArticleTag::getArticleId,id);
        List<ArticleTag> articleTags = articleTagService.list(wrapper);
        List<Long> tags=new ArrayList<>();
        for(ArticleTag articleTag:articleTags){
            tags.add(articleTag.getTagId());
        }
        articleDetaiAdminVo.setTags(tags);
        //封装响应返回
        return articleDetaiAdminVo;
    }

    @Override
    public void updateArticle(ArticleDetailAdminDto articleDetailAdminDto) {
        //把文章更新到Article表（包括访问量）
        Article newArticle = BeanCopyUtils.copyBean(articleDetailAdminDto, Article.class);
        updateById(newArticle);
        //把文章更新到Article_Tag表
        //条理先删再加我觉得可
        List<Long> tags = articleDetailAdminDto.getTags();
        articleTagService.remove(new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getArticleId,articleDetailAdminDto.getId()));
        for(Long tag:tags){
            articleTagService.save(new ArticleTag(articleDetailAdminDto.getId(),tag));
        }
        //把文章访问量更新到redis
        //查询博客信息
        List<Article> articles = getBaseMapper().selectList(null);
        Map<String , Integer> ViewCountMap = articles.stream()
                .collect(Collectors.toMap(article -> article.getId().toString(), article -> article.getViewCount().intValue()));
        //存储到redis
        redisCache.setCacheMap("article:viewCount",ViewCountMap);
    }

    @Override
    public void deleteById(Long id) {
        //逻辑删除 改变del_flag
        getBaseMapper().deleteById(id);
        //删除tag联系；
        articleTagService.deleteTagByArticleId(id);
        //删除有关评论 逻辑删除
        commentService.deleteCommentByArticleId(id);
    }
}
