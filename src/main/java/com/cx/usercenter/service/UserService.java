package com.cx.usercenter.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cx.usercenter.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author cx
* @description 针对表【user】的数据库操作Service
* @createDate 2023-12-20 13:33:21
*/
public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param checkPassword 用户校验密码
     * @return 值
     */
    Long userRegister(String userAccount,String userPassword,String checkPassword,String planetCode);

    /**
     * 用户登录
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @return 用户对象
     */
    User doLogin(String userAccount,String userPassword, HttpServletRequest request);

    /**
     * 根据用户名进行用户查询
     *
     * @param username 用户名
     * @param request 请求
     * @return 用户列表
     */
    List<User> searchUserByUsername(String username,HttpServletRequest request);

    /**
     * 根据id进行逻辑删除
     *
     * @param id      用户id
     * @param request 请求
     * @return 删除操作是否成功
     */
    Integer deleteById(Long id,HttpServletRequest request);

    /**
     * 返回脱敏的用户信息
     * @param request 请求
     * @return 脱敏的用户信息
     */
    User currentUser(HttpServletRequest request);

    /**
     * 登出
     * @param request 请求
     * @return 用户登出
     */
    Integer logout(HttpServletRequest request);

    /**
     * 根据标签来查询用户
     * @param tagNames
     * @return
     */
    List<User> getTagsBySQL(List<String> tagNames);

    /**
     * 更新用户信息
     * @param loginUser 当前登录用户信息
     * @param user 修改后的用户信息
     */
    void update(User loginUser, User user);

    /**
     * 输出所有用户
     * @return
     */
    Page<User> recommend();
}
