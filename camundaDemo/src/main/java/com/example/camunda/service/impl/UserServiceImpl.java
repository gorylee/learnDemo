package com.example.camunda.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.camunda.module.bo.UserQueryBo;
import com.example.camunda.dao.IUserDao;
import com.example.camunda.module.entity.User;
import com.example.camunda.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author GoryLee
* @date  2023-6-29
*/
@Service("userService")
public class UserServiceImpl extends ServiceImpl<IUserDao, User> implements IUserService {

    @Autowired
    private IUserDao dao;

    @Override
    public User get(UserQueryBo userQuery) {
        return dao.findOne(userQuery);
    }


    @Override
    public List<User> listAll(UserQueryBo userQuery) {
        return dao.findListAll(userQuery);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(User user) {
        dao.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(User user) {
        dao.insert(user);
    }
}
