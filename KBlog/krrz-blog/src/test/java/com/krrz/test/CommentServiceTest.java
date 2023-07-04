package com.krrz.test;

import com.krrz.domain.entity.Comment;
import com.krrz.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CommentServiceTest {
    @Autowired
    private CommentService commentService;
    @Test
    public void testCommentList(){

    }
    @Test
    public void testAddComment(){}

    @Test
    public void testDeleteComment(){}
}
