package com.example.xxjob.config;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.xxjob.utils.SpringUtils;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @Description
 * @Author GoryLee
 * @Date 2023/6/27
 */
@Configuration
@Slf4j
public class XxlJobConfig {

    @Value("${xxl.job.admin.addresses}")
    private String adminAddresses;

    @Value("${xxl.job.accessToken}")
    private String accessToken;

    @Value("${xxl.job.executor.appname}")
    private String appname;

    @Value("${xxl.job.executor.address}")
    private String address;

    @Value("${xxl.job.executor.ip}")
    private String ip;

    @Value("${xxl.job.executor.port}")
    private int port;

    @Value("${xxl.job.executor.logpath}")
    private String logPath;

    @Value("${xxl.job.executor.logretentiondays}")
    private int logRetentionDays;


    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        log.info(">>>>>>>>>>> start xxl-job config init. >>>>>>>>");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
        xxlJobSpringExecutor.setAppname(appname);
        xxlJobSpringExecutor.setAddress(address);
        xxlJobSpringExecutor.setIp(ip);
        xxlJobSpringExecutor.setPort(port);
        xxlJobSpringExecutor.setAccessToken(accessToken);
        xxlJobSpringExecutor.setLogPath(logPath);
        xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);
        log.info(">>>>>>>>>>> end xxl-job config init. >>>>>>>>");


        return xxlJobSpringExecutor;
    }

    @XxlJob("doJob")
//    @Transactional(rollbackFor = Exception.class)
    public void doJob(){
        String jobParam = XxlJobHelper.getJobParam();
        try {
            if(StrUtil.isNotBlank(jobParam)){
                JSONObject jsonObject = JSONObject.parseObject(jobParam);
                if(Objects.nonNull(jsonObject)){
                    Object bean = SpringUtils.getBean(jsonObject.getString("bean"));
                    Method method = bean.getClass().getMethod(jsonObject.getString("method"));
                    log.info("调度定时任务执行开始：{}",jobParam);
                    method.invoke(bean);
                    log.info("调度定时任务执行结束：{}",jobParam);
                }
            }
        }catch (Exception e){
            log.info("调度定时任务执行失败：{},原因是：{}",jobParam,e.getMessage());
            XxlJobHelper.handleFail();
        }

    }
}
