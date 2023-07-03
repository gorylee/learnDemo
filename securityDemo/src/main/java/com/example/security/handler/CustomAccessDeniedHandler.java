package com.example.security.handler;

import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSON;
import com.example.security.utils.WebUtil;
import example.common.model.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 授权过程中失败，捕获到异常，封装成统一的响应体格式返回
 * 注意：接口权限认证失败抛出的异常AccessDeniedException继承RuntimeException
 * 如果使用全局异常捕获RuntimeException，会返回全局异常捕获的信息，而不会返回自定义的权限异常信息
 * @Author GoryLee
 * @Date 2022/12/7 20:51
 * @Version 1.0
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        Result<Object> result = Result.createError(HttpStatus.HTTP_FORBIDDEN, "授权失败，你没权限操作");
        String jsonString = JSON.toJSONString(result);
        WebUtil.renderString(response,jsonString);
    }
}
