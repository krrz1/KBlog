package com.krrz.controller;

import com.krrz.domain.ResponseResult;
import com.krrz.domain.dto.AddArticleDto;
import com.krrz.domain.dto.ArticleDetailAdminDto;
import com.krrz.domain.dto.ListArticleDto;
import com.krrz.domain.vo.ArticleDetailAdminVo;
import com.krrz.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @PreAuthorize("@ps.hasPermission('content:article:writer')")
    @PostMapping
    public ResponseResult add(@RequestBody AddArticleDto articleDto){
        return articleService.add(articleDto);
    }
    @PreAuthorize("@ps.hasPermission('content:article:list')")
    @GetMapping("/list")
    public ResponseResult list(@RequestParam("pageNum") Long pageNum,@RequestParam("pageSize")Long pageSize,@RequestParam(required = false,name= "title",defaultValue = "")String title,@RequestParam(required = false,name= "summary",defaultValue = "")String summary){
        ListArticleDto listArticleDto=new ListArticleDto(pageNum,pageSize,title,summary);
        return articleService.listArticle(listArticleDto);
    }
    @GetMapping("/{id}")
    public ResponseResult queryArticleDetail(@PathVariable("id")Long id){
        ArticleDetailAdminVo articleDetailAdminVo=articleService.queryArticleById(id);
        return ResponseResult.okResult(articleDetailAdminVo);
    }
    @PutMapping
    public ResponseResult updateArticle(@RequestBody ArticleDetailAdminDto articleDetailAdminDto){
        articleService.updateArticle(articleDetailAdminDto);
        return ResponseResult.okResult();
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteArticleById(@PathVariable("id")Long id){
        articleService.deleteById(id);
        return ResponseResult.okResult();
    }
}
