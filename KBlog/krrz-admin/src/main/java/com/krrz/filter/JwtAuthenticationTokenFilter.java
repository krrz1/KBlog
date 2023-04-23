package com.krrz.filter;

import com.alibaba.fastjson.JSON;
import com.krrz.config.JwtUtil;
import com.krrz.config.RedisCache;
import com.krrz.config.WebUtils;
import com.krrz.domain.ResponseResult;
import com.krrz.domain.entity.LoginUser;
import com.krrz.enums.AppHttpCodeEnum;
import com.krrz.mapper.MenuMapper;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    RedisCache redisCache;
    @Autowired
    private MenuMapper menuMapper;
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //获取请求头中的token
        String token=httpServletRequest.getHeader("token");
        if(!StringUtils.hasText(token)){
            //说明该端口不需要token 直接放行
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }
        //解析获取userid
        Claims claims = null;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            //token超时  token非法
            //响应告诉前端需要重新登入
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(httpServletResponse, JSON.toJSONString(result));
            return ;
        }
        String userId = claims.getSubject();
        //从redis中获取用户信息
        LoginUser loginUser = redisCache.getCacheObject("login:" + userId);
        if(Objects.isNull(loginUser)){
            //说明登录过期
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(httpServletResponse, JSON.toJSONString(result));
            return ;
        }
        //将权限存入
        List<String> list = menuMapper.selectPermsByUserId(loginUser.getUser().getId());
        loginUser.setPermissions(list);
        //存入securityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(loginUser,null,null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
