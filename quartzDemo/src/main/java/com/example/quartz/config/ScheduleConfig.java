package com.example.quartz.config;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
public class ScheduleConfig {

    @Value("${schedule.enabled}")
    private boolean enabled;
    //如果使用业务的数据源
    @Autowired
    private DataSource dataSource;

    /**
     * 将一个quartz产生为Bean并交给Spring容器管理
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
        // Spring提供SchedulerFactoryBean为Scheduler提供配置信息,并被Spring容器管理其生命周期
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        //设置自动启动，默认为true
        factory.setAutoStartup(enabled);
        if(enabled){
            //这句一定要加！！！！不然properties配置不生效！！！！
            PropertiesFactoryBean propertiesFactoryBean=new PropertiesFactoryBean();
            propertiesFactoryBean.setLocation(new ClassPathResource("config/quartz.properties"));
            propertiesFactoryBean.afterPropertiesSet();
            factory.setQuartzProperties(propertiesFactoryBean.getObject());
            //定义数据源(在配置文件中定义,可单独配置数据源)
            factory.setDataSource(dataSource);
            //系统启动后延迟30秒启动定时任务
            factory.setStartupDelay(1);
            //可选，QuartzScheduler 启动时更新己存在的Job，这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了
            factory.setOverwriteExistingJobs(true);
        }
        return factory;
    }

    @Bean
    public Scheduler scheduler(SchedulerFactoryBean factoryBean) {
        return factoryBean.getScheduler();
    }
}
