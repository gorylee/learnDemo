package com.example.springbatch.job.listener;

import com.example.springbatch.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * @Description
 * @Author GoryLee
 * @Date 2024/9/14
 */
@Slf4j
public class CsvToDbJobListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        long beginTime = System.currentTimeMillis();
        jobExecution.getExecutionContext().putLong("begin",beginTime);
        log.info("-----作业开始时间【"+ TimeUtil.formatDate(beginTime) +"】---------------------");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        long beginTime = jobExecution.getExecutionContext().getLong("begin");
        long endTime = System.currentTimeMillis();
        log.info("-----作业结束时间【"+ TimeUtil.formatDate(endTime) +"】---------------------");
        log.info("-----作业总花费时间【"+ (endTime-beginTime) / 1000+"秒】---------------------");

    }
}
