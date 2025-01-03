package com.example.security.controller;

import com.example.security.exception.CustomException;
import com.example.security.model.bo.UserAddBo;
import com.example.security.model.bo.UserBo;
import com.example.security.model.entity.User;
import com.example.security.model.vo.LoginUser;
import com.example.security.service.UserService;
import com.example.security.utils.JwtUtil;
import example.common.model.Result;
import com.example.security.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    @PreAuthorize("@customExpression.hasAuthority('sys')")
    public Result<User> getUser(UserBo query) {
        User user = userService.getUser(query);
        return Result.createSuccess(user);
    }

    /**
     * {
     * "email": "12346@qi.com",
     * "role": "1",
     * "state": 1,
     * "userName": "lgy",
     * "password": "1234",
     * "sex": "男"
     * }
     */
    @PostMapping("/addUser")
    public Result<String> addUser(@RequestBody UserAddBo userBo) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode(userBo.getPassword());
        userBo.setPassword(password);
        userService.addUser(userBo);
        return Result.createSuccess("添加成功");
    }

    @PostMapping("/login")
    public Result<Map<String, String>> login(@RequestBody UserBo query) {

        //使用AuthenticationManager的认证接口惊醒用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(query.getUserName(), query.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (authenticate == null) {
            throw new CustomException("登录失败");
        }
        // 认证成功则通过jwt创建token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String token = JwtUtil.createToken(loginUser.getUser().getUserId(), loginUser.getUser().getUserName());
        Map<String, String> data = new HashMap<>();
        data.put("token", token);

        // 把用户信息存入到redis
        String key = "login:" + loginUser.getUser().getUserId();
        if (redisUtil.containsKey(key)) {
            redisUtil.del(key);
        }
        redisUtil.set(key, loginUser);
//        User user = (User)redisUtil.get("token_" + loginUser.getUser().getId());
        return Result.createSuccess(data);

    }

    @GetMapping("/logout")
    public Result<String> logout() {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        User user = (User)authentication.getPrincipal();
        redisUtil.del("login:"+user.getUserId());
        return Result.createSuccess("注销成功");
    }
}
