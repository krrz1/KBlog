package com.krrz;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
public class RedisTest {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Test
    public void test(){
//        stringRedisTemplate.opsForHash().delete("article:viewCount","155");
        Long id=1l;
        System.out.println(id.toString());
    }
}
