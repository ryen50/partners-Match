package com.cx.usercenter.controller;

import com.cx.usercenter.common.BaseResponse;
import com.cx.usercenter.common.ErrorCode;
import com.cx.usercenter.common.ResultUtils;
import com.cx.usercenter.entity.DTO.UserLoginDTO;
import com.cx.usercenter.entity.DTO.UserRegisterDTO;
import com.cx.usercenter.entity.User;
import com.cx.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户接口
 *
 * @author cx
 */
@Slf4j
@RequestMapping("/user")
@RestController
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 获取当前用户状态
     * @param request 请求
     * @return 脱敏后的用户信息
     */
    @GetMapping("/current")
    public User current(HttpServletRequest request)
    {
       return userService.currentUser(request);
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
            log.info("登录失败失败");
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
    public BaseResponse<List<User>> searchUser(String username,HttpServletRequest request)
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

    @PostMapping("/logout")
    public BaseResponse<Integer> logout(HttpServletRequest request)
    {
        if (request==null){
            return ResultUtils.error(ErrorCode.NULL_ERROR);
        }

        Integer logout = userService.logout(request);
        return ResultUtils.success(logout);
    }
}
