package com.example.camunda.controller;

import com.example.camunda.flow.service.FlowUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author GoryLee
 * @Date 2023/6/29
 */
@RestController
public class DemoController {

    @RequestMapping(value = "/demo")
    public String sayHello(){
        return "hello world";
    }
}
