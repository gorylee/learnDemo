package com.example.springbatch.job;

import com.example.springbatch.entity.Sale;
import com.example.springbatch.entity.SaleDetail;
import com.example.springbatch.job.processor.SaleDetailItemProcessor;
import com.example.springbatch.job.writer.CustomSaleWriter;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author GoryLee
 * @Date 2024/9/29
 */
@Configuration
public class SaleJob {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;


    @Bean
    public Job addSaleJob(Step insertMainStep, Step insertDetailStep) {
        return jobBuilderFactory.get("insertMainAndDetailJob")
                .incrementer(new RunIdIncrementer())
                .start(insertMainStep)
                .next(insertDetailStep)
                .build();
    }

    // Step 1: 插入主表
    @Bean
    public Step insertMainStep(ItemReader<Sale> reader, ItemProcessor<Sale, Sale> processor, CustomSaleWriter writer) {
        return stepBuilderFactory.get("insertMainStep")
                .<Sale, Sale>chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
    // Step 2: 插入明细表
    @Bean
    public Step insertDetailStep(ItemReader<SaleDetail> reader, ItemProcessor<SaleDetail, SaleDetail> processor, ItemWriter<SaleDetail> writer) {
        return stepBuilderFactory.get("insertDetailStep")
                .<SaleDetail, SaleDetail>chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }


    // 主表 ItemReader 示例
    @Bean
    @StepScope
    public ItemReader<Sale> mainItemReader() {
        List<Sale> saleList = new ArrayList<>();
        // 示例数据填充
        Sale sale1 = new Sale();
        sale1.setNo("BK1");
        sale1.setBusinessKey("BK1");
        saleList.add(sale1);

        Sale sale2 = new Sale();
        sale2.setNo("BK2");
        sale2.setBusinessKey("BK2");
        saleList.add(sale2);

        return new ListItemReader<>(saleList);
    }

    // 明细表 ItemReader 示例
    @Bean
    @StepScope
    public ItemReader<SaleDetail> detailItemReader() {
        List<SaleDetail> data = new ArrayList<>();
        // 示例数据填充
        SaleDetail saleDetail1 = new SaleDetail();
        saleDetail1.setBusinessKey("BK1");
        data.add(saleDetail1);

        SaleDetail saleDetail2 = new SaleDetail();
        saleDetail2.setBusinessKey("BK2");
        data.add(saleDetail2);

        return new ListItemReader<>(data);
    }

    // 主表 ItemProcessor 示例（无转换逻辑）
    @Bean
    public ItemProcessor<Sale, Sale> mainItemProcessor() {
        return item -> item;
    }

    // 明细表 ItemProcessor 示例
    @Bean
    public ItemProcessor<SaleDetail, SaleDetail> detailItemProcessor() {
        return new SaleDetailItemProcessor();
    }

    // 主表 ItemWriter 配置
    @Bean
    public CustomSaleWriter mainItemWriter() {
        CustomSaleWriter writer = new CustomSaleWriter();
        writer.setSqlSessionFactory(sqlSessionFactory);
        writer.setStatementId("com.example.springbatch.mapper.SaleMapper.saveSale");
//        writer.afterPropertiesSet();
        return writer;
    }

    // 明细表 ItemWriter 配置
    @Bean
    public MyBatisBatchItemWriter<SaleDetail> detailItemWriter() {
        MyBatisBatchItemWriter<SaleDetail> writer = new MyBatisBatchItemWriter<>();
        writer.setSqlSessionFactory(sqlSessionFactory);
        writer.setStatementId("com.example.springbatch.mapper.SaleMapper.saveSaleDetail");
//        writer.afterPropertiesSet();
        return writer;
    }
}
