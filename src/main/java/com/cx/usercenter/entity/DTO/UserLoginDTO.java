package com.cx.usercenter.entity.DTO;

import lombok.Data;

import java.io.Serializable;
@Data
public class UserLoginDTO implements Serializable {
    private static final long serialVersionUID= -8071368445182122667L;

    private String userAccount;
    private String userPassword;
}
