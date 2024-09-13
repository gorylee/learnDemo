package com.example.quartz.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.quartz.model.bo.CustomerAdditionalServicesQueryBo;
import com.example.quartz.model.entity.CustomerAdditionalServices;

import java.util.List;

/**
* @author GoryLee
* @date  2023-12-20
*/
public interface ICustomerAdditionalServicesService extends IService<CustomerAdditionalServices> {

    /**
    * 查询所有客户-附加服务关联表
    */
    List<CustomerAdditionalServices> listAll(CustomerAdditionalServicesQueryBo customerAdditionalServicesQuery);

}
