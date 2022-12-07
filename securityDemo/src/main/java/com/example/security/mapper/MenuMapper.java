package com.example.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.security.model.dto.UserPermissionDto;
import com.example.security.model.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author GoryLee
 * @Date 2022/12/7 20:18
 * @Version 1.0
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
    List<UserPermissionDto> getPermissionsByUserId(Long userId);
}
