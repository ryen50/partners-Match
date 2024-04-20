package com.cx.usercenter.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cx.usercenter.entity.User;
import com.cx.usercenter.service.UserService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class PreCacheJob {
    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private UserService userService;

    private List<Long> mainUserId= Arrays.asList(1L);

    @Scheduled(cron = "0 8 16 ? * * ")
    public void doCacheRecommendUser()
    {

        for(Long i:mainUserId)
        {
            Page<User> page = userService.page(new Page<>(1, 30), new QueryWrapper<>());
            ValueOperations valueOperations = redisTemplate.opsForValue();

            //设置层级
            String format = String.format("yupi:user:recommend:%s",i);

            valueOperations.set(format,page,30000, TimeUnit.MILLISECONDS);
        }


    }
}
