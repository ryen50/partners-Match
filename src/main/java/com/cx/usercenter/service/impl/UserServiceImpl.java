package com.cx.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cx.usercenter.common.ErrorCode;
import com.cx.usercenter.context.UserContext;
import com.cx.usercenter.entity.User;
import com.cx.usercenter.exception.BusinessException;
import com.cx.usercenter.mapper.UserMapper;
import com.cx.usercenter.service.UserService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author cx
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-12-20 13:33:21
*/
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{
    @Resource
    private UserMapper userMapper;

    @Override
    public Long userRegister(String userAccount, String userPassword, String checkPassword,String planetCode) {
        //校验账户和密码格式
        if(StringUtils.isAnyBlank(userAccount, userPassword,planetCode)) {
           throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        if (planetCode.length()>5)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"星球编号过长");
        }
        QueryWrapper<User> Wrapper = new QueryWrapper<>();
        Wrapper.eq("planet_code", planetCode);
        Long count = userMapper.selectCount(Wrapper);
        if (count>0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户重复" );
        }
        if (!(userPassword.equals(checkPassword)))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次密码不相同");
        }
         if(checkAccountAndPassword(userAccount, userPassword)!=UserContext.SUCCESS)
         {
             throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号密码不合格式，账号至少有一个字母一个数字");
         }
         //查询是否重复
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_account", userAccount);
        Long count1 = userMapper.selectCount(userQueryWrapper);
        if (count1>0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号重复");
        }


        //将密码进行MD5加密
        String s = DigestUtils.md5DigestAsHex((UserContext.SALT+userPassword).getBytes());
        //封装数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setAvatarUrl("");
        user.setGender(0);
        user.setPlanetCode(planetCode);
        user.setUserPassword(s);
        int insert = userMapper.insert(user);
        if (insert<0)
        {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return user.getId();
    }

    /**
     * 校验账户和密码
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @return -1L为不和格式 1L为符合格式
     */
    private Integer checkAccountAndPassword(String userAccount, String userPassword) {

        String regexAccount="^[a-zA-Z][a-zA-Z0-9_]{4,15}$";
        if (!(userAccount.matches(regexAccount)))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号不合格式，账号至少有一个字母一个数字");
        }
        if (!(userAccount.length()>=4&& userAccount.length()<16))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户长度不合要求");
        }
        if (!(userPassword.length()>=8&& userPassword.length()<=16))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码长度不合要求");
        }
        return UserContext.SUCCESS;
    }

    @Override
    public User doLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //校验用户名和密码
        if (checkAccountAndPassword(userAccount,userPassword)!=UserContext.SUCCESS) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号密码不合格式");

        }
        //查询用户
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_account",userAccount);

        //将密码进行MD5加密
        userPassword=DigestUtils.md5DigestAsHex((UserContext.SALT+userPassword).getBytes());
        //查询用户是否存在
        userQueryWrapper.eq("user_password",userPassword);
        User user = userMapper.selectOne(userQueryWrapper);
        if (user==null)
        {
            log.info("user login failed,userAccount Cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号或密码不正确");
        }
        //用户脱敏
        User safetyUser = userTOSafety(user);


        //更改用户登录状态
        request.getSession().setAttribute(UserContext.USER_LOGIN_STATE,safetyUser);

        return safetyUser;
    }

    /**
     * 用户信息脱敏
     * @param user
     * @return
     */
    private static User userTOSafety(User user) {
        if (user==null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUserName(user.getUserName());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setGender(user.getGender());
        safetyUser.setRole(user.getRole());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setEmail(user.getTags());
        return safetyUser;
    }

    @Override
    public List<User> searchUserByUsername(String username, HttpServletRequest request) {
        //先鉴权
        if (!isAdmin(request)){
            log.info("非管理员访问查询方法");
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username))
        {
            queryWrapper.like("user_name",username);
        }
        List<User> userList = userMapper.selectList(queryWrapper);
        //返回数据脱敏
        return userList.stream().peek(UserServiceImpl::userTOSafety).collect(Collectors.toList());

    }

    /**
     * 根据用户删除id
     * @param id      用户id
     * @param request 请求
     * @return
     */
    @Override
    public Integer deleteById(Long id, HttpServletRequest request) {
        //先鉴权
        if (!isAdmin(request)){
            log.info("非管理员访问删除方法");
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        if (id<0)
        {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        return userMapper.deleteById(id);
    }

    /**
     * 获取当前用户
     * @param request 请求
     * @return
     */
    @Override
    public User currentUser(HttpServletRequest request) {
        Object objAttribute = request.getSession().getAttribute(UserContext.USER_LOGIN_STATE);
        User user = (User) objAttribute;
        if (user==null)
        {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        Long id = user.getId();
        User user1 = userMapper.selectOne(new QueryWrapper<User>().eq("id", id));
        return userTOSafety(user1);
    }

    @Override
    public Integer logout(HttpServletRequest request) {
        request.getSession().removeAttribute(UserContext.USER_LOGIN_STATE);
        return UserContext.SUCCESS;
    }

    /**
     * 根据id来查询用户
     * @param tagNames 标签名
     * @return
     */
    @Override
    public List<User> getUserByTags(List<String> tagNames) {
        //先对形参列表检查非空
        if (CollectionUtils.isEmpty(tagNames))
            throw  new BusinessException(ErrorCode.NULL_ERROR);
        //根据tag进行sql模糊查询
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        for (String tagName : tagNames) {
            wrapper.like("tags",tagName);
        }

        List<User> userList = userMapper.selectList(wrapper);

//        //先对形参列表检查非空
//        if (CollectionUtils.isEmpty(tagNames))
//            throw  new BusinessException(ErrorCode.NULL_ERROR);
//
//        Gson gson = new Gson();
//        //查询所有用户的标签
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        List<User> userList = userMapper.selectList(queryWrapper);
//        //在内存中将标签从json改为集合并查询是否全部包含参数中的标签
//        return userList.stream().filter(user -> {
//                    if (StringUtils.isBlank(user.getTags()))
//                        return false;
//                    Set<String> userTagsSet=gson.fromJson(user.getTags(),new TypeToken<Set<String>>(){}.getType());
//                    for (String tagName : tagNames) {
//                        if (!userTagsSet.contains(tagName))
//                            return false;
//                    }
//                    return true;
//                }
//        ).map(UserServiceImpl::userTOSafety).collect(Collectors.toList());

        return  userList.stream().map(UserServiceImpl::userTOSafety).collect(Collectors.toList());
    }


    /**
     * 判断访问者权限
     * @param request 请求
     * @return
     */
    private boolean isAdmin(HttpServletRequest request){
        Object attribute = request.getSession().getAttribute(UserContext.USER_LOGIN_STATE);
        User user = (User) attribute;
        return user.getRole()==UserContext.IS_ADMIN;
    }
}




