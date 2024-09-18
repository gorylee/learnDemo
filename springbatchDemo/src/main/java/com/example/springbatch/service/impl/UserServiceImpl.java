package com.example.springbatch.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springbatch.entity.User;
import com.example.springbatch.mapper.UserMapper;
import com.example.springbatch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    private static final int BATCH_SIZE = 5000;

    @Override
    public void saveUserTempBatch(List<User> users){
        int total = users.size();
        for (int i = 0; i < total; i += BATCH_SIZE) {
            int end = Math.min(total, i + BATCH_SIZE);
            List<User> batchList = users.subList(i, end);
            userMapper.saveUserTempBatch(batchList);
        }
    }

    @Override
    public void saveUserTemp(User user){
        userMapper.saveUserTemp(user);
    }

    @Override
    public void truncateAll() {
        userMapper.truncateAll();
    }

    @Override
    public void truncateAllTemp() {
        userMapper.truncateAllTemp();
    }


}
