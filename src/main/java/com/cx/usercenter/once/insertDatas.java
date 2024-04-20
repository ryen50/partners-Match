package com.cx.usercenter.once;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.cx.usercenter.entity.User;
import com.cx.usercenter.mapper.UserMapper;
import com.cx.usercenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;


@Component
public class insertDatas {
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserService userService;
    public void insert()
    {
        System.out.println("执行定时方法");
        int count=2500;
        StopWatch stopWatch=new StopWatch();
        stopWatch.start();

        int j=0;
        //创建一个线程安全的列表
        List<User> list = Collections.synchronizedList(new ArrayList<>());
        //创建一个用来存储CompletableFuture的列表
        ArrayList<CompletableFuture<Void>> futures = new ArrayList<>();

        for(int i=0;i<40;i++)
        while (true){
            j++;

            User user = new User();
            user.setUserName("mock");
            user.setUserAccount("123123123");
            user.setAvatarUrl("");
            user.setGender(0);
            user.setUserPassword("12345678");
            user.setPhone("13011112222");
            user.setEmail("12312312@qq.com");
            user.setUserStatus(0);
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
            user.setTags("");
            user.setIsDelete(0);
            user.setRole(0);
            user.setProfile("哒咩");
            list.add(user);

            if(j%count==0) break;
        }
       //异步的执行插入任务
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() ->
        {
            userService.saveBatch(list, 10000);
        });

        futures.add(voidCompletableFuture);
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[]{})).join();
        stopWatch.stop();
        System.out.println(stopWatch.getLastTaskTimeMillis());
    }
}
