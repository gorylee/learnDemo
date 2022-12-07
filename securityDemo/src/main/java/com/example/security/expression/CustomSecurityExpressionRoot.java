package com.example.security.expression;

import cn.hutool.core.util.StrUtil;
import com.example.security.model.vo.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author GoryLee
 * @Date 2022/12/7 22:33
 * @Version 1.0
 */
@Component("customExpression")
public class CustomSecurityExpressionRoot {


    public final boolean hasAuthority(String per) {
        // 从redis中获取用户权限 或者SecurityContextHolder中获取
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        List<String> permissions = loginUser.getPermissions();
        for (String permission : permissions) {
            if(StrUtil.contains(permission,per)){
                return true;
            }
        }
        return false;
    }
}
