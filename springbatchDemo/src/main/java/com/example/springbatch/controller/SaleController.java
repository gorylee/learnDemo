package com.example.springbatch.controller;

import com.example.springbatch.mapper.UserMapper;
import example.common.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @Description
 * @Author GoryLee
 * @Date 2024/9/14
 */
@RestController
@RequestMapping("/job")
@Slf4j
public class SaleController {

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    @Qualifier("addSaleJob")
    private Job addSaleJob;



    @GetMapping("/addSale")
    public Result<BatchStatus> addSale() throws Exception{
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        JobExecution run = jobLauncher.run(addSaleJob, jobParameters);
        return Result.createSuccess(run.getStatus());
    }

}
