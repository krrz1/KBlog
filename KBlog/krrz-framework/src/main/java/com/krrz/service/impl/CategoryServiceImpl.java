package com.krrz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krrz.constans.SystemConstants;
import com.krrz.domain.ResponseResult;
import com.krrz.domain.dto.AddCategoryDto;
import com.krrz.domain.dto.ListCategoryDto;
import com.krrz.domain.entity.Article;
import com.krrz.domain.entity.Category;
import com.krrz.domain.entity.Tag;
import com.krrz.domain.vo.CategoryVo;
import com.krrz.domain.vo.GetCategoryVo;
import com.krrz.domain.vo.PageVo;
import com.krrz.mapper.CategoryMapper;
import com.krrz.service.ArticleService;
import com.krrz.service.ArticleService2;
import com.krrz.service.CategoryService;
import com.krrz.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2022-11-05 17:18:05
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    @Lazy
    private ArticleService articleService;
    @Override
    public ResponseResult getCategoryList() {
        //查询文章表,状态为已发布的文章
        LambdaQueryWrapper<Article> articleWrapper=new LambdaQueryWrapper<>();
        articleWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(articleWrapper);
        //获取文章的分类id，并且去重
        Set<Long> categoryIds = articleList.stream()
                .map(article -> article.getCategoryId())
                .collect(Collectors.toSet());
        //查询分类表
        List<Category> categories=listByIds(categoryIds);
        categories=categories.stream().filter(category ->SystemConstants.STATUS_NORMAL.equals(category.getStatus())).collect(Collectors.toList());
        //封装vo
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
        return ResponseResult.okResult(categories);
    }

    @Override
    public List<CategoryVo> listAllCategory() {
        LambdaQueryWrapper<Category> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Category::getStatus,SystemConstants.STATUS_NORMAL);
        List<Category> categories = list(wrapper);
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);

        return categoryVos;
    }

    @Override
    public ResponseResult listCategory(Long pageNum, Long pageSize, ListCategoryDto listCategoryDto) {
        //分页查询
        Page<Category> page=new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        LambdaQueryWrapper<Category> wrapper=new LambdaQueryWrapper<>();

        wrapper.eq(StringUtils.hasText(listCategoryDto.getName()),Category::getName,listCategoryDto.getName());
        wrapper.eq(StringUtils.hasText(listCategoryDto.getStatus()),Category::getStatus,listCategoryDto.getStatus());

        page(page, wrapper);
        //封装数据返回
        PageVo pageVo=new PageVo(page.getRecords(),page.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addCategory(AddCategoryDto addCategoryDto) {
        Category category = BeanCopyUtils.copyBean(addCategoryDto, Category.class);
        save(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getCategoryById(Long id) {
        Category category = getById(id);
        GetCategoryVo getCategoryVo = BeanCopyUtils.copyBean(category, GetCategoryVo.class);
        return ResponseResult.okResult(getCategoryVo);
    }

    @Override
    public ResponseResult updateCategory(GetCategoryVo categoryDto) {
        Category category = BeanCopyUtils.copyBean(categoryDto, Category.class);
        updateById(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteById(Long id) {
        getBaseMapper().deleteById(id);
        return ResponseResult.okResult();
    }
}

