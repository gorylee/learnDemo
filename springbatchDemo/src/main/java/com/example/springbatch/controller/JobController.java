package com.example.springbatch.controller;

import com.example.springbatch.mapper.UserMapper;
import example.common.model.Result;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.Date;

/**
 * @Description
 * @Author GoryLee
 * @Date 2024/9/14
 */
@RestController
@RequestMapping("/job")
@Slf4j
public class JobController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    @Qualifier("csvToDbJob")
    private Job csvToDbJob;
    @Autowired
    @Qualifier("dbToDbJob")
    private Job dbToDbJob;

    @Autowired
    private JobExplorer jobExplorer;



    @GetMapping("/csvToDb")
    public Result<BatchStatus> csvToDb() throws Exception{
        userMapper.truncateAllTemp();
        JobParameters jobParameters = new JobParametersBuilder(new JobParameters(), jobExplorer)
                .addLong("time", System.currentTimeMillis())
                .getNextJobParameters(csvToDbJob)
                .toJobParameters();

        JobExecution run = jobLauncher.run(csvToDbJob, jobParameters);
        return Result.createSuccess(run.getStatus());
    }


    @GetMapping("/dbToDb")
    public Result<BatchStatus> dbToDb() throws Exception{
        userMapper.truncateAll();
        JobParameters jobParameters = new JobParametersBuilder(new JobParameters(), jobExplorer)
                .addLong("time", System.currentTimeMillis())
                .getNextJobParameters(dbToDbJob)
                .toJobParameters();

        JobExecution run = jobLauncher.run(dbToDbJob, jobParameters);
        return Result.createSuccess(run.getStatus());
    }
}
