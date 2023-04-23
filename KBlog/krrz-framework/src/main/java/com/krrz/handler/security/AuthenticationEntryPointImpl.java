package com.krrz.handler.security;

import com.alibaba.fastjson.JSON;
import com.krrz.config.WebUtils;
import com.krrz.domain.ResponseResult;
import com.krrz.enums.AppHttpCodeEnum;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        e.printStackTrace();
        //响应给前端
        ResponseResult result=null;
        //InsufficientAuthenticationException   BadCredentialsException
        if(e instanceof BadCredentialsException){
            result= ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR.getCode(),e.getMessage());
        }else if(e instanceof InsufficientAuthenticationException){
            result=ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }else{
            result=ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "认证失败");
        }

        WebUtils.renderString(httpServletResponse, JSON.toJSONString(result));
    }
}
