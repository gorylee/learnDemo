package com.example.xxjob.service.impl;

import com.example.xxjob.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author GoryLee
 * @Date 2023/6/27
 */
@Service("userService")
@Slf4j
public class UserServiceImpl implements UserService {
    @Override
    public void demoJob() {
        log.info(">>>>>>>>>> 开始处理实际业务逻辑 >>>>>>>>>>>>>>>");
        log.info(">>>>>>>>>> 完成处理实际业务逻辑 >>>>>>>>>>>>>>>");
    }
}
