package com.krrz.test;

import com.krrz.domain.entity.User;
import com.krrz.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

public class UserServiceTest {

    @Test
    public void testUserInfor(){
    }
    @Test
    public void testUpdateInfo(){
        User user=new User();
    }
    @Test
    public void testRegister(){
        String s="14787164048675";
        Long i2=Long.parseLong(s);
        Long i=14787164048675l;
        System.out.println(i2.equals(i));
    }
}
