package com.example.quartz.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.quartz.model.bo.ScheduleQueryBo;
import com.example.quartz.model.entity.Schedule;

import java.util.List;

/**
* @author GorryLee
*/
public interface IScheduleService extends IService<Schedule> {

    /**
    * 增加定时任务
    */
    void add(Schedule schedule);

    /**
    * 获取定时任务
    */
    Schedule get(ScheduleQueryBo scheduleQuery);

    /**
    * 编辑定时任务
    */
    void edit(Schedule schedule);

    /**
    * 分页查询定时任务
    */
    Page<Schedule> list(ScheduleQueryBo scheduleQuery);

    /**
    * 查询所有定时任务
    */
    List<Schedule> listAll(ScheduleQueryBo scheduleQuery);
}
