package com.cx.usercenter.service.impl;

import com.cx.usercenter.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

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
}
