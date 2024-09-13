package com.example.quartz.controller;

import cn.hutool.core.bean.BeanUtil;
import com.example.quartz.model.bo.CustomerAdditionalServicesQueryBo;
import com.example.quartz.model.entity.CustomerAdditionalServices;
import com.example.quartz.model.vo.CustomerAdditionalServicesVo;
import com.example.quartz.model.vo.ScheduleVo;
import com.example.quartz.service.ICustomerAdditionalServicesService;
import example.common.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description
 * @Author GoryLee
 * @Date 2023/12/29
 */
@RestController
@RequestMapping("/customerAdditional")
public class CustomerAdditionalServicesController {


    @Autowired
    private ICustomerAdditionalServicesService customerAdditionalServicesService;

    @GetMapping("/listAll")
    //@Permission("sa:customerAdditional:list")
    public Result<List<CustomerAdditionalServicesVo>> listAll(CustomerAdditionalServicesQueryBo customerAdditionalServicesQuery){
        List<CustomerAdditionalServices> customerAdditionalServicesList = customerAdditionalServicesService.listAll(customerAdditionalServicesQuery);
        List<CustomerAdditionalServicesVo> list = BeanUtil.copyToList(customerAdditionalServicesList, CustomerAdditionalServicesVo.class);
        return Result.createSuccess(list);
    }
}
