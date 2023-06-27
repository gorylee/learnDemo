package com.example.xxjob.job;

import com.example.xxjob.service.UserService;
import com.example.xxjob.service.impl.UserServiceImpl;
import com.xxl.job.core.handler.IJobHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description
 * @Author GoryLee
 * @Date 2023/6/27
 */
@Slf4j
public class DemoHandler extends IJobHandler {

    private final UserService userService = new UserServiceImpl();

    @Override
    public void execute() throws Exception {
        log.info(">>>>>>>>>> BEAN模式（类形式） 开始 >>>>>>>>>>>>>>>");
        userService.demoJob();
        log.info(">>>>>>>>>> BEAN模式（类形式） 成功 >>>>>>>>>>>>>>>");
    }
}
