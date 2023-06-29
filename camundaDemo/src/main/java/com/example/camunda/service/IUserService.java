package com.example.camunda.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.camunda.module.bo.UserQueryBo;
import com.example.camunda.module.entity.User;

import java.util.List;

/**
* @author GoryLee
* @date  2023-6-29
*/
public interface IUserService extends IService<User> {

    /**
     * 新增用户信息表
     */
    void add(User user);

    /**
     * 查询用户信息表详情
     */
    User get(UserQueryBo userQuery);

    /**
     * 编辑用户信息表
     */
    void edit(User user);


    /**
     * 查询所有用户信息表
     */
    List<User> listAll(UserQueryBo userQuery);

}
