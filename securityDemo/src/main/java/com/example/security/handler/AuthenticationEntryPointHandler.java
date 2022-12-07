package com.example.security.handler;

import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSON;
import com.example.security.utils.WebUtil;
import example.common.model.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证过程中失败，捕获到异常，封装成统一的响应体格式返回
 * @Author GoryLee
 * @Date 2022/12/7 20:44
 * @Version 1.0
 */
@Component
public class AuthenticationEntryPointHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        Result<Object> result = Result.createError(HttpStatus.HTTP_UNAUTHORIZED, "认证失败请重新登录");
        String jsonString = JSON.toJSONString(result);
        WebUtil.renderString(response,jsonString);
    }
}
