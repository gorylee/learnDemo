package com.example.quartz.controller;

import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.quartz.convert.ScheduleConvert;
import com.example.quartz.exception.CustomException;
import com.example.quartz.model.bo.ScheduleAddBo;
import com.example.quartz.model.bo.ScheduleEditBo;
import com.example.quartz.model.bo.ScheduleQueryBo;
import com.example.quartz.model.entity.Schedule;
import com.example.quartz.model.vo.Ids;
import com.example.quartz.model.vo.ScheduleVo;
import com.example.quartz.service.IScheduleService;
import example.common.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 定时任务控制器
 * @author GorryLee
 */
@Slf4j
@Api(value = "定时任务", tags = "定时任务")
@RestController
@RequestMapping("/sys/schedule")
public class ScheduleController {

    @Autowired
    private IScheduleService scheduleService;

    @ApiOperation(value = "查询定时任务", notes = "查询定时任务")
    @ApiOperationSupport(author = "GorryLee")
    @GetMapping("/get")
    //@Permission("sys:schedule:get")
    public Result<ScheduleVo> get(ScheduleQueryBo scheduleQuery){
        Schedule schedule = scheduleService.get(scheduleQuery);
        if(schedule==null){
            throw new CustomException("未查询到定时任务");
        }
        ScheduleVo scheduleVo = ScheduleConvert.INSTANCE.scheduleToScheduleVo(schedule);
        return Result.createSuccess(scheduleVo);
    }


    @ApiOperation(value = "查询定时任务列表", notes = "查询定时任务列表")
    @ApiOperationSupport(author = "GorryLee")
    @GetMapping("/page")
    public Result page(ScheduleQueryBo scheduleQuery){
        Page<Schedule> page = scheduleService.list(scheduleQuery);
        return Result.createSuccess(page);
    }

    @ApiOperation(value = "查询全部定时任务列表", notes = "查询全部定时任务列表")
    @ApiOperationSupport(author = "GorryLee")
    @GetMapping("/listAll")
    public Result<List<ScheduleVo>> listAll(ScheduleQueryBo scheduleQuery){
        return Result.createSuccess(ScheduleConvert.INSTANCE.scheduleListToScheduleVoList(scheduleService.listAll(scheduleQuery)));
    }

    @ApiOperation(value = "新增定时任务", notes = "新增定时任务")
    @ApiOperationSupport(author = "GorryLee")
    @PostMapping("/add")
    public Result<String> add(@RequestBody @Valid ScheduleAddBo scheduleAddBo){
        Schedule schedule = ScheduleConvert.INSTANCE.scheduleAddBoToSchedule(scheduleAddBo);
        scheduleService.add(schedule);
        return Result.createSuccess();
    }

    @ApiOperation(value = "修改定时任务", notes = "修改定时任务")
    @ApiOperationSupport(author = "GorryLee")
    @PostMapping("/edit")
    public Result<String> edit(@RequestBody @Valid ScheduleEditBo scheduleEditBo){
        Schedule schedule = ScheduleConvert.INSTANCE.scheduleEditBoToSchedule(scheduleEditBo);
        scheduleService.edit(schedule);
        return Result.createSuccess();
    }

    @ApiOperation(value = "删除定时任务",notes = "删除定时任务")
    @ApiOperationSupport(author = "GorryLee")
    @PostMapping("/delete")
    public Result<String> delete(@RequestBody Ids ids){
        if (ArrayUtils.isEmpty(ids.getIds())) {
            return Result.createSuccess("id不能为空");
        }
        List<Schedule> scheduleList=new ArrayList<>();
        for(Long id : ids.getIds()){
            Schedule schedule=new Schedule();
            schedule.setId(id);
            schedule.setDeleted(1);
            scheduleList.add(schedule);
        }
        scheduleService.updateBatchById(scheduleList);
        return Result.createSuccess();
    }
}
