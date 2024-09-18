package com.example.springbatch.job.config;

import com.example.springbatch.entity.User;
import com.example.springbatch.job.listener.CsvToDbJobListener;
import com.example.springbatch.service.UserService;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import java.io.File;

/**
 * @Description
 * @Author GoryLee
 * @Date 2024/9/14
 */
@Configuration
public class CsvToDbJobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;
    @Autowired
    private UserService userService;

    @Value("${job.data.path}")
    private String path;

    // 定义itemReader
    @Bean
    public FlatFileItemReader<User> csvToDbItemReader(){
        return new FlatFileItemReaderBuilder<User>()
                .name("csvToDbItemReader")
                .saveState(false)
                .resource(new PathResource(new File(path,"user.csv").getAbsolutePath()))
                .delimited()
                .names("id","name","age","addr")
                .targetType(User.class)
                .build();
    }

    // 定义itemWriter
    @Bean
    public MyBatisBatchItemWriter<User> csvToDbItemWriter(){
        return new MyBatisBatchItemWriterBuilder<User>()
                .sqlSessionFactory(sqlSessionFactory)
                .statementId("com.example.springbatch.mapper.UserMapper.saveUserTemp")
                .build();
    }

    // 定义Step
    @Bean
    public Step csvToDbStep(){
        return stepBuilderFactory.get("csvToDbStep")
                .<User,User>chunk(10000)
                .reader(csvToDbItemReader())
//                .writer(userService::saveUserTempBatch) // 使用 UserService 执行批量插入
                .writer(csvToDbItemWriter())
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    // 定义Job
    @Bean
    public Job csvToDbJob(){
        return jobBuilderFactory.get("csvToDbJob")
                .start(csvToDbStep())
                .incrementer(new RunIdIncrementer())
                .listener(new CsvToDbJobListener())
                .build();
    }
}
