package com.example.springbatch.job.config;

import com.example.springbatch.entity.User;
import com.example.springbatch.job.listener.CsvToDbJobListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author GoryLee
 * @Date 2024/9/18
 */
@Configuration
@Slf4j
public class DbToDbJobConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    // 从步骤
    @Bean
    @StepScope
    public MyBatisPagingItemReader<User> dbToDbItemReader(
            @Value("#{stepExecutionContext[from]}") Integer from,
            @Value("#{stepExecutionContext[to]}") Integer to,
            @Value("#{stepExecutionContext[range]}") Integer range){
        log.info("------MyBatisPagingItemReader开始----from："+from+"----to："+to+"-----每片数量:"+range);
        MyBatisPagingItemReader<User> itemReader = new MyBatisPagingItemReader<>();
        itemReader.setSqlSessionFactory(sqlSessionFactory);
        itemReader.setPageSize(1000);
        itemReader.setQueryId("com.example.springbatch.mapper.UserMapper.selectUserTempList");
        Map<String,Object> map = new HashMap<>();
        map.put("from",from);
        map.put("to",to);
        itemReader.setParameterValues(map);
        return itemReader;
    }

    @Bean
    public MyBatisBatchItemWriter<User> dbToDbItemWriter(){
        return new MyBatisBatchItemWriterBuilder<User>()
                .sqlSessionFactory(sqlSessionFactory)
                .statementId("com.example.springbatch.mapper.UserMapper.saveUser")
                .build();
    }

    // 定义Step
    @Bean
    public Step workerStep(){
        return stepBuilderFactory.get("dbToDb_workerStep")
                .<User,User>chunk(10000)
                .reader(dbToDbItemReader(null,null,null))
                .writer(dbToDbItemWriter())
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    // 分区器
    @Bean
    public Partitioner dbToDbPartitioner(){
        return new DbToDbPartitioner();
    }

    // 分区处理器
    @Bean
    public PartitionHandler dbToDbPartitionerHandler(){
        TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
        handler.setGridSize(50);
        handler.setStep(workerStep());
        handler.setTaskExecutor(new SimpleAsyncTaskExecutor());
        try {
            handler.afterPropertiesSet();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return handler;
    }

    // 主步骤
    @Bean
    public Step masterStep(){
        return stepBuilderFactory.get("dbToDb_masterStep")
                .partitioner(workerStep().getName(),dbToDbPartitioner())
                .partitionHandler(dbToDbPartitionerHandler())
                .build();
    }

    // 作业
    @Bean
    public Job dbToDbJob(){
        return jobBuilderFactory.get("dbToDbJob")
                .start(masterStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }
}
