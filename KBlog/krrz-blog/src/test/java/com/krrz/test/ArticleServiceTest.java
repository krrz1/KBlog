package com.krrz.test;

import com.krrz.config.RedisCache;
import com.krrz.domain.ResponseResult;
import com.krrz.domain.dto.AddArticleDto;
import com.krrz.domain.vo.*;
import com.krrz.service.ArticleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.AssertionsKt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class ArticleServiceTest {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private RedisCache redisCache;

    @Test
    public void testHostArticleList(){
//        ResponseResult result = articleService.hostArticleList();
//        List<HotArticleVo> articleVos= (List<HotArticleVo>) result.getData();
//        Assertions.assertEquals(articleVos.get(0).getId(),1);
    }
    @Test
    public void testArticleList(){
//        ResponseResult result = articleService.articleList(1,10,null);
//        PageVo data = (PageVo) result.getData();
//        List<ArticleListVo> rows = data.getRows();
//        ArticleListVo articleListVo = rows.get(0);
//        Assertions.assertEquals(articleListVo.getId(),1);
    }
    @Test
    public void testGetArticleDetail(){
//        ArticleDetailVo articleDetail = articleService.getArticleDetail(1l);
//        Assertions.assertEquals(articleDetail.getTitle(),"SpringSecurity框架的record");
    }

    @Test
    public void testUpdateViewCount(){
//        Map<String, Object> cacheMap = redisCache.getCacheMap("article:viewCount");
//        Integer num = (Integer) cacheMap.get(1);
//        articleService.updateViewCount(1l);
//        cacheMap = redisCache.getCacheMap("article:viewCount");
//        Integer newNum = (Integer) cacheMap.get(1);
//        Assertions.assertEquals(num+1,newNum);
    }

    @Test
    public void testAdd(){
//        AddArticleDto articleDto=new AddArticleDto(null,"1111","1111","!!!",1l,null,"0","0",null,null);
//        articleService.add(articleDto);
//        ArticleDetailAdminVo articleDetailAdminVo = articleService.queryArticleById(28l);
//        String title = articleDetailAdminVo.getTitle();
//        Assertions.assertEquals(title,"1111");
    }

    @Test
    public void testDeleteById(){
//        try{
//            articleService.deleteById(28l);
//            articleService.queryArticleById(28l);
//        }catch (Exception e){
//            Assertions.assertTrue(1==1);
//        }

    }

}
