package com.cx.usercenter.exception;

import com.cx.usercenter.common.BaseResponse;
import com.cx.usercenter.common.ErrorCode;
import com.cx.usercenter.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e)
    {
        log.error("businessException"+e.getMessage()+e);
        return ResultUtils.error(e.getCode(),e.getMessage(),e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(BusinessException e)
    {
        log.error("runtimeException"+e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR);
    }

}
