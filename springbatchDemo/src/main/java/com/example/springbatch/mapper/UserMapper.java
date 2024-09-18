package com.example.springbatch.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springbatch.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    void saveUserTempBatch(@Param(value = "userList") List<User> userList);

    void truncateAll();

    void truncateAllTemp();

    void saveUserTemp(User user);

}
