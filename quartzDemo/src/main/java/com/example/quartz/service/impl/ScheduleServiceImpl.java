package com.example.quartz.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.quartz.job.ScheduleJob;
import com.example.quartz.mapper.IScheduleMapper;
import com.example.quartz.model.bo.ScheduleQueryBo;
import com.example.quartz.model.entity.Schedule;
import com.example.quartz.service.IScheduleService;
import example.common.enums.BoolEnum;
import example.common.exception.ResultException;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

/**
* @author GorryLee
*/
@Slf4j
@Service
public class ScheduleServiceImpl extends ServiceImpl<IScheduleMapper, Schedule> implements IScheduleService {

    @Value("${schedule.enabled:false}")
    private boolean enabled;

    @Autowired
    private IScheduleMapper dao;

    @Autowired
    private Scheduler scheduler;

    /**
     * 服务启动，加载定时任务
     */
    @PostConstruct
    public void initSchedule(){
        if(enabled){
            //加载状态为启用的定时任务
            ScheduleQueryBo scheduleQueryBo = new ScheduleQueryBo().setStatus(1);
            scheduleQueryBo.setDeleted(BoolEnum.FALSE.getKey());
            List<Schedule> schedules = this.listAll(scheduleQueryBo);
            if(CollUtil.isNotEmpty(schedules)){
                try {
                    scheduler.clear();
                } catch (SchedulerException e) {
                    log.error("警告：定时任务启动异常！");
                }
                schedules.forEach(e ->{
                    CronTrigger cronTrigger = getCronTrigger(e);
                    JobDetail jobDetail = getJobDetail(e);
                    //添加到任务调度器
                    try {
                        scheduler.scheduleJob(jobDetail,cronTrigger);
                    } catch (SchedulerException ex) {
                        log.error("警告：定时任务加载异常！[class={}, method={}]", e.getClassName(), e.getMethodName());
                    }
                });
            }
        }
    }

    /**
     * 组装JobDetail
     */
    private JobDetail getJobDetail(Schedule schedule){
        JobDetail jobDetail = JobBuilder.newJob(ScheduleJob.class)
                .withIdentity(getJobKey(schedule))
                .withDescription(schedule.getRemark())
                .build();
        jobDetail.getJobDataMap().put(jobDetail.getKey().getName(),schedule);
        return jobDetail;
    }
    /**
     * 获取jobKey
     */
    private JobKey getJobKey(Schedule schedule) {
        return JobKey.jobKey(schedule.getClassName().concat(".").concat(schedule.getMethodName()));
    }

    /**
     * 组装CronTrigger
     */
    private static CronTrigger getCronTrigger(Schedule schedule) {
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(schedule.getCron());
        return TriggerBuilder.newTrigger()
                .withIdentity(TriggerKey.triggerKey(schedule.getClassName().concat(".").concat(schedule.getMethodName())))
                .withSchedule(cronScheduleBuilder)
                .build();
    }

    /**
     * 添加定时任务
     * @param schedule
     */
    private void addSchedule(Schedule schedule){
        if(enabled){
            try{
                if(scheduler.checkExists(getJobKey(schedule))){
                    throw new ResultException("已存在任务调度:".concat(schedule.getClassName()).concat(".").concat(schedule.getMethodName()));
                }
                scheduler.scheduleJob(getJobDetail(schedule),getCronTrigger(schedule));
            }catch (Exception e){
                log.error("添加任务失败：[class={}, method={}]", schedule.getClassName(), schedule.getMethodName(),e);
                throw new ResultException("任务添加失败:".concat(schedule.getClassName()).concat(".").concat(schedule.getMethodName()));
            }

        }
    }

    /**
     * 移除定时任务
     */
    private void removeSchedule(Schedule schedule) {
        JobKey jobKey = getJobKey(schedule);
        try{
            if(scheduler.checkExists(getJobKey(schedule))){
                CronTrigger cronTrigger = getCronTrigger(schedule);
                scheduler.pauseTrigger(cronTrigger.getKey()); // 停止触发器
                scheduler.unscheduleJob(cronTrigger.getKey());// 移除触发器
                scheduler.deleteJob(jobKey);  // 删除任务
            }
        }catch (Exception e){
            log.error("移除任务失败：[class={}, method={}]", schedule.getClassName(), schedule.getMethodName(),e);
            throw new ResultException("移除任务失败:".concat(schedule.getClassName()).concat(".").concat(schedule.getMethodName()));
        }
    }


    /**
     * 暂停任务
     */
    @Transactional(rollbackFor = Exception.class)
    public void pauseSchedule(Schedule schedule){
        schedule.setStatus(-1);
        dao.updateById(schedule);
        if(enabled){
            removeSchedule(schedule);
        }
    }

    /**
     * 恢复任务
     */
    @Transactional(rollbackFor = Exception.class)
    public void resumeSchedule(Schedule schedule){
        schedule.setStatus(1);
        dao.updateById(schedule);
        if(enabled){
            addSchedule(schedule);
        }
    }

    /**
     * 执行任务一次(临时）
     */
    public void runOnce(Schedule schedule){
        if(enabled){
            JobKey jobKey = getJobKey(schedule);
            try {
                if(scheduler.checkExists(jobKey)){
                    //在任务调度中
                    scheduler.triggerJob(jobKey);
                }else {
                    //不在任务调度中
                    JobDetail jobDetail = getJobDetail(schedule);// 获取JobDetail并设置JobDataMap参数
                    // 临时执行一次，不要设置运行周期
                    Trigger trigger = TriggerBuilder.newTrigger()
                            .startNow()
                            .withIdentity(TriggerKey.triggerKey("TEMP:".concat(schedule.getClassName()).concat(".").concat(schedule.getMethodName())))
                            .build();
                    scheduler.scheduleJob(jobDetail, trigger);
                }
            }catch (Exception e){
                log.error("临时执行任务失败：[class={}, method={}]", schedule.getClassName(), schedule.getMethodName(),e);
                throw new ResultException("临时执行任务失败:".concat(schedule.getClassName()).concat(".").concat(schedule.getMethodName()));
            }

        }
    }

    /**
     * 任务详情
     */
    @Override
    public Schedule get(ScheduleQueryBo scheduleQuery) {
        return dao.findOne(scheduleQuery);
    }

    /**
     * 任务列表(分页)
     */
    @Override
    public Page<Schedule> list(ScheduleQueryBo scheduleQuery) {
        Page<Schedule> page = new Page<>(scheduleQuery.getPageNo(),scheduleQuery.getPageSize());
        List<Schedule> scheduleList = dao.findList(page,scheduleQuery);
        page.setRecords(scheduleList);
        return page;
    }

    /**
     * 任务列表
     */
    @Override
    public List<Schedule> listAll(ScheduleQueryBo scheduleQuery) {
        return dao.findListAll(scheduleQuery);
    }


    /**
     * 修改任务（改了时间cron、class、method）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(Schedule schedule) {
        Schedule scheduleOld = this.getById(schedule.getId());
        if(scheduleOld==null){
            throw new ResultException("定时任务不存在"+schedule.getId());
        }
        dao.updateById(schedule);
        if(enabled){
            //删除任务
            removeSchedule(scheduleOld);
            //新增任务
            addSchedule(schedule);
        }

    }

    /**
     * 增加任务
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(Schedule schedule) {
        dao.insert(schedule);
        //添加到任务列表
        if(BoolEnum.TRUE.getKey().equals(schedule.getStatus())){
            addSchedule(schedule);
        }
    }


    public void testJob(){
        System.out.println("-----------hello world-------------");
    }
}
