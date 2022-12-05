package com.example.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author GoryLee
 * @Date 2022/12/4 20:23
 * @Version 1.0
 */
@RestController
public class HelloController {

    @GetMapping(value = "/hello")
    public String sayHello(){
        return "hello world";
    }
}
