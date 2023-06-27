package com.example.xxjob.job;

import com.example.xxjob.service.UserService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @XxlJob("demoJobHandler")
    public void demoJobHandler() throws Exception {
        log.info(">>>>>>>>>> BEAN模式（类形式） 开始 >>>>>>>>>>>>>>>");
        userService.demoJob();
        log.info(">>>>>>>>>> BEAN模式（类形式） 成功 >>>>>>>>>>>>>>>");
    }
}
