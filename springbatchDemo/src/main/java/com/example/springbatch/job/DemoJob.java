package com.example.springbatch.job;

import com.example.springbatch.service.UserService;
import com.xxl.job.core.handler.annotation.XxlJob;
import example.common.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author GoryLee
 * @Date 2023/6/27
 */
@Component
@Slf4j
public class DemoJob {

    @Autowired
    private UserService userService;

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    @Qualifier("dbToDbJob")
    private Job dbToDbJob;

    @Autowired
    private JobExplorer jobExplorer;

    @XxlJob("dbToDbJobHandler")
    public void dbToDbJobHandler() throws Exception {
        log.info(">>>>>>>>>> BEAN模式（类形式） 开始 >>>>>>>>>>>>>>>");
        userService.truncateAll();
        JobParameters jobParameters = new JobParametersBuilder(new JobParameters(), jobExplorer)
                .addLong("time", System.currentTimeMillis())
                .getNextJobParameters(dbToDbJob)
                .toJobParameters();

        JobExecution run = jobLauncher.run(dbToDbJob, jobParameters);
        log.info(">>>>>>>>>> 作业调度完成，状态："+run.getStatus()+" >>>>>>>>>>>>>>>");
        log.info(">>>>>>>>>> BEAN模式（类形式） 成功 >>>>>>>>>>>>>>>");
    }
}
