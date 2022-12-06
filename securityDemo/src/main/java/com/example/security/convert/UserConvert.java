package com.example.security.convert;

import com.example.security.model.bo.UserAddBo;
import com.example.security.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface UserConvert {

    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    User userAddBoToUser(UserAddBo userAddBo);

}

