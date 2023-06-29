package com.example.camunda.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.camunda.module.bo.UserQueryBo;
import com.example.camunda.module.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author GoryLee
* @date  2023-6-29
*/
@Mapper
public interface IUserDao extends BaseMapper<User> {

    User findOne(@Param("query") UserQueryBo userQuery);

    List<User> findListAll(@Param("query") UserQueryBo userQuery);

}
