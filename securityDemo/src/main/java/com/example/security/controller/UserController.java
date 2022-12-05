package com.example.security.controller;

import com.example.security.model.bo.UserBo;
import com.example.security.model.entity.User;
import com.example.security.service.UserService;
import example.common.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/getUser")
    public Result getUser(UserBo query){
        User user =userService.getUser(query);
        return Result.createSuccess(user);
    }

}
