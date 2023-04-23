package com.krrz.handler.exception;

import com.krrz.domain.ResponseResult;
import com.krrz.enums.AppHttpCodeEnum;
import com.krrz.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SystemException.class)
    public ResponseResult systemExceptionHandle(SystemException e){
        //打印异常信息
        log.error("出现了异常{}",e);
        //从异常对象中获取信息封装返回
        return ResponseResult.errorResult(e.getCode(),e.getMsg() );
    }

    @ExceptionHandler(Exception.class)
    public ResponseResult ExceptionHandle(Exception e){
        //打印异常信息
        log.error("出现了异常{}",e);
        //从异常对象中获取信息封装返回
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),e.getMessage());
    }
}
