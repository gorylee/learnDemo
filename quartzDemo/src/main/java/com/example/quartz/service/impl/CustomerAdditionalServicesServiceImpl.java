package com.example.quartz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.quartz.mapper.ICustomerAdditionalServicesMapper;
import com.example.quartz.model.bo.CustomerAdditionalServicesQueryBo;
import com.example.quartz.model.entity.CustomerAdditionalServices;
import com.example.quartz.service.ICustomerAdditionalServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author GoryLee
* @date  2023-12-20
*/
@Service("customerAdditionalServicesService")
public class CustomerAdditionalServicesServiceImpl extends ServiceImpl<ICustomerAdditionalServicesMapper, CustomerAdditionalServices> implements ICustomerAdditionalServicesService {

    @Autowired
    private ICustomerAdditionalServicesMapper mapper;


    @Override
    public List<CustomerAdditionalServices> listAll(CustomerAdditionalServicesQueryBo customerAdditionalServicesQuery) {
        return mapper.findListAll(customerAdditionalServicesQuery);
    }


}
