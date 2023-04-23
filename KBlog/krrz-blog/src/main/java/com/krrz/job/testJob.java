package com.krrz.job;

import org.junit.jupiter.api.Disabled;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Deprecated
//@Component
public class testJob {
    @Scheduled(cron = "0/5 * * * * ?" )
    public void testJob(){
        System.out.println("定时任务启动了");
    }
}
