package com.example.security.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.security.mapper.MenuMapper;
import com.example.security.mapper.UserMapper;
import com.example.security.model.dto.UserPermissionDto;
import com.example.security.model.entity.User;
import com.example.security.model.vo.LoginUser;
import example.common.exception.ResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author GoryLee
 * @Date 2022/12/5 01:51
 * @Version 1.0
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        // 查询用户认证信息
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .eq(User::getUserName, userName);
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            throw new ResultException("用户名或者密码错误");
        }

//        List<String> permissions = ListUtil.toLinkedList("test", "admin");
        List<UserPermissionDto> list = menuMapper.getPermissionsByUserId(user.getUserId());
        List<String> permissions = null;
        if (CollUtil.isNotEmpty(list)) {
            permissions = list.stream().map(UserPermissionDto::getPerms)
                    .collect(Collectors.toList());
        }
        // 封装UserDetails
        LoginUser loginUser = new LoginUser(user, permissions);
        return loginUser;
    }
}
