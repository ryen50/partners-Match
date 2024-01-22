package com.cx.usercenter.common;

public class ResultUtils {
    /**
     * 请求成功
     * @param data 请求的数据
     * @return 通用的响应体
     * @param <T> 泛型
     */
    public static<T> BaseResponse<T> success(T data)
    {
       return new BaseResponse<T>(0,data,"ok");
    }

    /**
     * 请求失败
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode)
    {
        return new BaseResponse(errorCode);
    }
    public static BaseResponse error(int code,String massage,String description)
    {
        return new BaseResponse(code,null,massage,description);
    }
    public static BaseResponse error(ErrorCode errorCode,String description)
    {
        return new BaseResponse(errorCode,description);
    }

}
