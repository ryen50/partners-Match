package com.cx.usercenter.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Controller;

import java.io.Serializable;

/**
 * 基本返回类
 * code 为返回代码
 * data 为返回的数据
 * massage 为返回的报错信息
 * @param <T>
 */
@Data
public class BaseResponse<T> implements Serializable {

    private int code;
    private T data;
    private String massage;
    private String description;

    public BaseResponse(int code, T data, String massage, String description) {
        this.code = code;
        this.data = data;
        this.massage = massage;
        this.description = description;
    }

    public BaseResponse(int code, T data) {
      this(code,data,"","");
    }

    public BaseResponse(int code, T data, String massage) {
        this(code,data,massage,"");
    }

    BaseResponse (ErrorCode errorCode)
    {
        this(errorCode.getCode(),null,errorCode.getMessage(),errorCode.getDescription());
    }
    BaseResponse (ErrorCode errorCode,String description)
    {
        this(errorCode.getCode(),null,errorCode.getMessage(),description);
    }

    BaseResponse (ErrorCode errorCode,String massage,String description)
    {
        this(errorCode.getCode(),null,massage,description);
    }
}
