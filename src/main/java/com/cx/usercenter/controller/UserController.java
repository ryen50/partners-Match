package com.cx.usercenter.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cx.usercenter.common.BaseResponse;
import com.cx.usercenter.common.ErrorCode;
import com.cx.usercenter.common.ResultUtils;
import com.cx.usercenter.context.UserContext;
import com.cx.usercenter.entity.DTO.UserLoginDTO;
import com.cx.usercenter.entity.DTO.UserRegisterDTO;
import com.cx.usercenter.entity.DTO.searchDataDTO;
import com.cx.usercenter.entity.User;
import com.cx.usercenter.exception.BusinessException;
import com.cx.usercenter.service.UserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 用户接口
 *
 * @author cx
 */
@Slf4j
@RequestMapping("/user")
@RestController
@CrossOrigin(origins = "http://localhost:5173",allowCredentials = "true")
//@CrossOrigin
@Api(tags = "用户相关接口")
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 获取当前用户状态
     * @param request 请求
     * @return 脱敏后的用户信息
     */
    @GetMapping("/current")
    public  BaseResponse<User> current(HttpServletRequest request)
    {
       return ResultUtils.success(userService.currentUser(request));
    }



    /**
     * 用户注册
     * @param userRegisterDTO
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> register(@RequestBody UserRegisterDTO userRegisterDTO)
    {
        Long userId = userService.userRegister(userRegisterDTO.getUserAccount(), userRegisterDTO.getUserPassword(), userRegisterDTO.getCheckPassword(),userRegisterDTO.getPlanetCode());
        if (userId==-1L)
        {
            log.info("注册失败");
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }
        return ResultUtils.success(userId);
    }

    /**
     * 用户登录
     *
     * @param userLoginDTO
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<User> login(@RequestBody UserLoginDTO userLoginDTO, HttpServletRequest request)
    {
        log.info("用户登录");
        User user = userService.doLogin(userLoginDTO.getUserAccount(), userLoginDTO.getUserPassword(),request);
        if (user==null)
        {
            log.info("登录失败");
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }
        return ResultUtils.success(user);
    }

    /**
     * 查找用户
     * @param username
     * @param request
     * @return
     */
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUser( String username,HttpServletRequest request)
    {
        List<User> userList = userService.searchUserByUsername(username, request);
        return ResultUtils.success(userList);
    }

    /**
     * 删除用户
     * @param id
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Integer> deleteById(Long id,HttpServletRequest request)
    {
        Integer integer = userService.deleteById(id, request);
        return ResultUtils.success(integer);
    }

    /**
     * 用户登出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> logout(HttpServletRequest request)
    {
        if (request==null){
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }

        Integer logout = userService.logout(request);
        return ResultUtils.success(logout);
    }

    /**
     * 按照标签搜索
     * @param tagNameList
     * @return
     */
    @PostMapping("/search/tags")
    public BaseResponse<List<User>> getByTags(@RequestBody searchDataDTO tagNameList)
    {
        if (tagNameList.getTagNameList()==null) throw new BusinessException(ErrorCode.NULL_ERROR);

        List<User> userList = userService.getTagsBySQL(tagNameList.getTagNameList());

        return ResultUtils.success(userList);
    }

    /**
     * 更改用户信息
     * @param user 新的用户信息
     * @param request 请求
     * @return
     */
    @PostMapping("/update")
    public BaseResponse update(@RequestBody User user,HttpServletRequest request)
    {
        if (user==null)
            throw new BusinessException(ErrorCode.NULL_ERROR);

        User loginUser = (User) request.getSession().getAttribute(UserContext.USER_LOGIN_STATE);
        if (loginUser==null) throw new BusinessException(ErrorCode.NOT_LOGIN);

        userService.update(loginUser,user);

        return ResultUtils.success();
    }

    /**
     * 按照标签搜索
     * @return
     */
    @GetMapping("/recommend")
    public BaseResponse<Page<User>> recommend(HttpServletRequest request)
    {
        User loginUser=userService.currentUser(request);
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String format = String.format("yupi:user:recommend:%s",loginUser.getId());
        Object o = valueOperations.get(format);

        if(o!=null)
        {
           return ResultUtils.success((Page<User>) valueOperations.get(format));
        }else {
            valueOperations.set(format,userService.recommend(),30000, TimeUnit.MILLISECONDS);
            return ResultUtils.success(userService.recommend());
        }

    }
}
