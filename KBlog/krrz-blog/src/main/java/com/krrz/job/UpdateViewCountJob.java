package com.krrz.job;

import com.krrz.config.RedisCache;
import com.krrz.domain.entity.Article;
import com.krrz.service.ArticleService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UpdateViewCountJob {
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleService articleService;

    @Scheduled(cron = "0 0/10 * * * ?" )
    public void updateViewCount(){
        //获取redis中的浏览量
        Map<String, Integer> cacheMap = redisCache.getCacheMap("article:viewCount");

        List<Article> articleList = cacheMap.entrySet()
                .stream()
                .map(stringIntegerEntry -> new Article(Long.valueOf(stringIntegerEntry.getKey()), stringIntegerEntry.getValue().longValue()))
                .collect(Collectors.toList());
        //更新到数据库中
        articleService.updateBatchById(articleList);
    }
}
