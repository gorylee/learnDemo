package com.example.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.security.model.bo.UserAddBo;
import com.example.security.model.bo.UserBo;
import com.example.security.model.entity.User;

public interface UserService extends IService<User> {

    User getUser(UserBo query);

    void addUser(UserAddBo userBo);
}
