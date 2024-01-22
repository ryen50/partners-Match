package com.cx.usercenter.context;

public class UserContext {
     /**
      * md5公共盐码
      */
     public static final String SALT="yupi";
     /**
      * 用户登录状态
      */
     public static final String USER_LOGIN_STATE="userLoginState";
     /**
      * 管理员权限
      */
     public static final int IS_ADMIN=1;
     public static final int NOT_IS_ADMIN=0;
     /**
      * 成功
      */
     public static final int SUCCESS=1;
     public static final int FAILURE=0;
}
