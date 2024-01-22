package com.cx.usercenter.service.impl;

import com.cx.usercenter.entity.User;
import com.cx.usercenter.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class UserServiceTest {
    @Resource
    private UserService userService;
    @Test
    public void testRegister()
    {
        String userAccount="yupi";
        String userPassword="";
        String checkPassword="123456";

//        Long result = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertEquals(-1,result);
//
//        userAccount="12";
//        result = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertEquals(-1,result);
//        userAccount="1 2";
//        result = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertEquals(-1,result);
//
//        userPassword="123123123";
//        result = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertEquals(-1,result);
//
//        userAccount="douyupi";
//        checkPassword="12345678";
//        result = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertEquals(-1,result);
//
//        userPassword="12345678";
//        result = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertTrue(result>0);


    }
    @Test
    public void test2()
    {
        List<String> list = Arrays.asList("java", "python");
        List<User> userByTags = userService.getUserByTags(list);
        Assertions.assertTrue(userByTags.size()>0);
    }
}
