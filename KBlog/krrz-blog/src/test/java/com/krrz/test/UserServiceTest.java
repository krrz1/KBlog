package com.krrz.test;

import com.krrz.domain.entity.User;
import com.krrz.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void testUserInfor(){
    }
    @Test
    public void testUpdateInfo(){
        User user=new User();
    }
    @Test
    public void testRegister(){
    }
    @Test
    public void testListUser(){

    }
    @Test
    public void testAddUser(){

    }
    @Test
    public void testDeleteById(){

    }
    @Test
    public void testUpdateUser(){

    }
}
