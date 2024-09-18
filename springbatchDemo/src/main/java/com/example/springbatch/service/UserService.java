package com.example.springbatch.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springbatch.entity.User;

import java.util.List;

public interface UserService extends IService<User> {

    void saveUserTempBatch(List<User> users);
    void saveUserTemp(User user);

    void truncateAll();

    void truncateAllTemp();
}
