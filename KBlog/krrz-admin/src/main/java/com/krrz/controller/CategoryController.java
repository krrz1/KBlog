package com.krrz.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.fastjson.JSON;
import com.krrz.domain.ResponseResult;
import com.krrz.domain.dto.AddCategoryDto;
import com.krrz.domain.dto.ListCategoryDto;
import com.krrz.domain.entity.Category;
import com.krrz.domain.vo.CategoryVo;
import com.krrz.domain.vo.ExcelCategoryVo;
import com.krrz.domain.vo.GetCategoryVo;
import com.krrz.enums.AppHttpCodeEnum;
import com.krrz.service.CategoryService;
import com.krrz.utils.BeanCopyUtils;
import com.krrz.utils.WebUtils;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    //写博文中查询所有分类
    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        List<CategoryVo> list=categoryService.listAllCategory();
        return ResponseResult.okResult(list);
    }

    //导出excel
    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("/export")
    public void export(HttpServletResponse response){

        try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("Category.xlsx",response);
            //获取需要导出的数据
            List<Category> category=categoryService.list();

            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(category, ExcelCategoryVo.class);
            //把数据写入到excel中
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);
        } catch (Exception e) {
            //如果出现异常也要响应json
            //e.printStackTrace();
            ResponseResult result=ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            com.krrz.config.WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }
    @PreAuthorize("@ps.hasPermission('content:category:list')")
    @GetMapping("/list")
    public ResponseResult list(@RequestParam("pageNum")Long pageNum, @RequestParam("pageSize")Long pageSize, ListCategoryDto listCategoryDto){
        return categoryService.listCategory(pageNum,pageSize,listCategoryDto);
    }
    @PostMapping
    public ResponseResult addCategory(@RequestBody AddCategoryDto addCategoryDto){
        return categoryService.addCategory(addCategoryDto);
    }
    @GetMapping("/{id}")
    public ResponseResult getCategoryById(@PathVariable("id")Long id){
        return categoryService.getCategoryById(id);
    }
    @PutMapping
    public ResponseResult updateCategory(@RequestBody GetCategoryVo categoryDto){
        return categoryService.updateCategory(categoryDto);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteById(@PathVariable("id")Long id){
        return categoryService.deleteById(id);
    }
}
