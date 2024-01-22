package com.cx.usercenter.entity.DTO;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册接收类
 * @author cx
 */
@Data
public class UserRegisterDTO implements Serializable {
    private static final long serialVersionUID= 7491955168316047945L;

    private String userAccount ;
    private String userPassword;
    private String checkPassword;
    private String planetCode;

}
