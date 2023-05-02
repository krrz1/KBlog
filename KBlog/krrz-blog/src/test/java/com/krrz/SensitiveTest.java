package com.krrz;

import com.krrz.utils.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SensitiveTest {
    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void test(){
        String text="小黑子喜欢搞黄色，wtfuck！";
        String filter = sensitiveFilter.filter(text);
        System.out.println(filter);
    }
}
