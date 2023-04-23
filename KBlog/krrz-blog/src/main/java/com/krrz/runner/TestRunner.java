package com.krrz.runner;

import io.lettuce.core.dynamic.annotation.CommandNaming;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
@Deprecated
//@Component
public class TestRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("程序初始化");
    }
}
