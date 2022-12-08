package com.example.quartz.job;

import cn.hutool.extra.spring.SpringUtil;
import com.example.quartz.model.entity.Schedule;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import java.lang.reflect.Method;

@Slf4j
@DisallowConcurrentExecution//告诉Quartz在成功执行了Job实现类的execute方法后（没有发生任何异常），
// 更新JobDetail中JobDataMap的数据，使得该JobDetail实例在下一次执行的时候，JobDataMap中是更新后的数据，而不是更新前的旧数据
@PersistJobDataAfterExecution//告诉Quartz不要并发地执行同一个JobDetail实例
public class ScheduleJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Schedule schedule = (Schedule) context.getMergedJobDataMap().get(context.getJobDetail().getKey().getName());
        if (schedule == null) {
            log.warn("警告！没有获取到定时任务信息，请立即检查！[jobDetail={}]", context.getJobDetail());
            return;
        }
        try {
            log.info("定时任务执行开始{}",context.getJobDetail());
            Object object = SpringUtil.getBean(Class.forName(schedule.getClassName()));
            Method method = object.getClass().getMethod(schedule.getMethodName());
            method.invoke(object);
            log.info("定时任务执行完成{}",context.getJobDetail());
        } catch (Exception e) {
            log.error("定时任务出现异常！[class={}, method={} : {}]", schedule.getClassName(), schedule.getMethodName(),e);
        }
    }
}
