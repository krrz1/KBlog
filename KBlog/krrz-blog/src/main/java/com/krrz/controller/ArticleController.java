package com.krrz.controller;

import com.krrz.domain.ResponseResult;
import com.krrz.domain.vo.ArticleDetailVo;
import com.krrz.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @GetMapping("/hotArticleList")
    public ResponseResult hostArticleList(){
        //查询热门文章封装成
        ResponseResult result=articleService.hostArticleList();
        return result;
    }

    @GetMapping("/articleList")
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId){
        return articleService.articleList(pageNum,pageSize,categoryId);
    }
    @GetMapping("/{id}")
    public ResponseResult getArticleDetail(@PathVariable("id") Long id){
        ArticleDetailVo articleDetailVo =articleService.queryWithLogicExpire(id);
        //封装响应返回
        return ResponseResult.okResult(articleDetailVo);
    }
    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable("id") Long id){
        return articleService.updateViewCount(id);
    }
}
