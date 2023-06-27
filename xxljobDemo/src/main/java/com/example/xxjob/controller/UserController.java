package com.example.xxjob.controller;

import com.example.xxjob.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Description
 * @Author GoryLee
 * @Date 2023/6/27
 */
@RestController
public class UserController {
    @Resource
    private UserService userService;

    @RequestMapping("/test")
    public void test(){
        userService.demoJob();
    }
}
