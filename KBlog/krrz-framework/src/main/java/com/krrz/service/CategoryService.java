package com.krrz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.krrz.domain.ResponseResult;
import com.krrz.domain.dto.AddCategoryDto;
import com.krrz.domain.dto.ListCategoryDto;
import com.krrz.domain.entity.Category;
import com.krrz.domain.vo.CategoryVo;
import com.krrz.domain.vo.GetCategoryVo;

import java.util.List;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2022-11-05 17:18:04
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    List<CategoryVo> listAllCategory();

    ResponseResult listCategory(Long pageNum, Long pageSize, ListCategoryDto listCategoryDto);

    ResponseResult addCategory(AddCategoryDto addCategoryDto);

    ResponseResult getCategoryById(Long id);

    ResponseResult updateCategory(GetCategoryVo categoryDto);

    ResponseResult deleteById(Long id);
}
