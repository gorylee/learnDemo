package com.example.security.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.security.convert.UserConvert;
import com.example.security.mapper.UserMapper;
import com.example.security.model.bo.UserAddBo;
import com.example.security.model.bo.UserBo;
import com.example.security.model.entity.User;
import com.example.security.service.UserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author GoryLee
 * @since 2022-11-14
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User getUser(UserBo query) {

        User user = this.getById(query.getUserId());
        return user;
    }

    @Override
    public void addUser(UserAddBo userBo) {
        User user = UserConvert.INSTANCE.userAddBoToUser(userBo);
        this.save(user);
    }
}
