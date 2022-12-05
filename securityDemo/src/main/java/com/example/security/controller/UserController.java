package com.example.security.controller;

import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.security.model.bo.UserBo;
import com.example.security.model.entity.User;
import com.example.security.model.vo.LoginUser;
import com.example.security.service.UserService;
import example.common.model.Result;
import example.common.utils.JwtUtil;
import com.example.security.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author GoryLee
 * @Date 2022/12/5 00:51
 * @Version 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/getUser")
    public Result getUser(UserBo query) {
        User user = userService.getUser(query);
        return Result.createSuccess(user);
    }

    @PostMapping("/login")
    public Result login(@RequestBody UserBo query) {

        //使用AuthenticationManager的认证接口惊醒用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(query.getUserName(), query.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (authenticate == null) {
            throw new RuntimeException("登录失败");
        }
        // 认证成功则通过jwt创建token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String token = JwtUtil.createToken(loginUser.getUser().getId(), loginUser.getUser().getUserName());
        Map<String,String> data = new HashMap<>();
        data.put("token",token);

        // 把用户信息存入到redis
        String key = "token_"+loginUser.getUser().getId();
        if(redisUtil.containsKey(key)){
            redisUtil.del(key);
        }
        redisUtil.set(key,loginUser.getUser());
//        JSONObject jsonObject = (JSONObject)redisUtil.get("token_" + loginUser.getUser().getId());
//        User user = jsonObject.toJavaObject(User.class);
        return Result.createSuccess(data);

    }

}
