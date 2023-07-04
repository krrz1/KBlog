package com.krrz.test;

import com.krrz.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CategoryServiceTest {
    @Autowired
    private CategoryService categoryService;

    @Test
    public void testGetCategoryById(){

    }
    @Test
    public void testUpdateCategory(){}
    @Test
    public void testDeleteById(){}
    @Test
    public void testAddCategory(){}
}
