package com.cx.usercenter.service.impl;

import com.cx.usercenter.entity.User;
import com.cx.usercenter.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class UserServiceTest {
    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate redisTemplate;
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
        List<User> userByTags = userService.getTagsBySQL(list);
        Assertions.assertTrue(userByTags.size()>0);
    }

    @Test
    public void redisTest()
    {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("yupii","adada");
    }
}
