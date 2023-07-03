package com.example.camunda.controller;

import com.example.camunda.module.bo.UserAddBo;
import com.example.camunda.module.bo.UserQueryBo;
import com.example.camunda.module.entity.User;
import com.example.camunda.module.vo.JsonResult;
import com.example.camunda.service.IUserService;
import com.example.camunda.utils.AssertUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户信息表控制器
 * @author GoryLee
 * @date  2023-6-29
 */
@Slf4j
@Api(value = "用户信息表User", tags = "用户信息表User")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @ApiOperation(value = "查询用户信息表", notes = "查询用户信息表")
    @ApiOperationSupport(author = "GoryLee")
    @GetMapping("/get")
    public JsonResult<User> get(UserQueryBo userQuery){
        User user = userService.get(userQuery);
        AssertUtils.isNull(user,"未查询到用户信息表");
        return JsonResult.ok(user);
    }

    @ApiOperation(value = "查询用户信息表", notes = "查询用户信息表")
    @ApiOperationSupport(author = "GoryLee")
    @RequestMapping("/add")
    public JsonResult get(@RequestBody  UserAddBo userAddBo){
        User user = new User();
        BeanUtils.copyProperties(userAddBo,user);
        userService.add(user);
        return JsonResult.ok(user);
    }


}
