package com.example.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.quartz.model.bo.CustomerAdditionalServicesQueryBo;
import com.example.quartz.model.entity.CustomerAdditionalServices;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author GoryLee
* @date  2023-12-20
*/
@Mapper
public interface ICustomerAdditionalServicesMapper extends BaseMapper<CustomerAdditionalServices> {
    List<CustomerAdditionalServices> findListAll(@Param("query") CustomerAdditionalServicesQueryBo customerAdditionalServicesQuery);

}
