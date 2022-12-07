package com.example.security.handler;

import cn.hutool.core.util.StrUtil;
import com.example.security.exception.CustomException;
import com.example.security.model.vo.LoginUser;
import com.example.security.utils.RedisUtil;
import example.common.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token认证过滤器
 * @Author GoryLee
 * @Date 2022/12/6 21:34
 * @Version 1.0
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 获取token
        String token = request.getHeader("token");
        if (StrUtil.isEmpty(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        // 解析token获取userId
        Long userId;
        try {
            Claims claims = JwtUtil.parseToken(token);
            userId = claims.get("userId", Long.class);
        } catch (Exception e) {
            throw new CustomException("非法token");
        }
        // 从redis中获取用户信息
        LoginUser loginUser = redisUtil.get("login:" + userId);
        if(loginUser == null ){
            throw new CustomException("用户未登录");
        }
        // 存入SecurityContextHolder认证
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser,null,loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
