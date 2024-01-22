package com.cx.usercenter;
import java.util.Date;

import com.cx.usercenter.entity.User;
import com.cx.usercenter.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class UserCenterApplicationTests {
    @Resource
    private UserService userService;

    @Test
    void contextLoads() {
        User user = new User();
        user.setUserName("123123");
        user.setUserAccount("123");
        user.setAvatarUrl("123");
        user.setGender(0);
        user.setUserPassword("123");
        user.setPhone("12312");
        user.setEmail("123123");

        boolean save = userService.save(user);
        Assertions.assertTrue(save);
    }
    @Test
    void testMd5()
    {
        String user="12312312312312";
        String s = DigestUtils.md5DigestAsHex(user.getBytes());
        System.out.println(s);
    }

}
