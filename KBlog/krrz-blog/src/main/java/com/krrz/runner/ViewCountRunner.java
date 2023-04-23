package com.krrz.runner;

import com.krrz.config.RedisCache;
import com.krrz.domain.entity.Article;
import com.krrz.mapper.ArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ViewCountRunner implements CommandLineRunner {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private RedisCache redisCache;
    @Override
    //在启动服务时
    public void run(String... args) throws Exception {
        //查询博客信息
        List<Article> articles = articleMapper.selectList(null);
        Map<String , Integer> ViewCountMap = articles.stream()
                .collect(Collectors.toMap(article -> article.getId().toString(), article -> article.getViewCount().intValue()));
        //存储道redis
        redisCache.setCacheMap("article:viewCount",ViewCountMap);
    }
}
