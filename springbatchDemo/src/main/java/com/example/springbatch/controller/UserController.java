package com.example.springbatch.controller;

import cn.hutool.core.io.IoUtil;
import com.example.springbatch.entity.User;
import com.example.springbatch.service.UserService;
import com.example.springbatch.utils.TimeUtil;
import example.common.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Description
 * @Author GoryLee
 * @Date 2024/9/14
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @Value("${job.data.path}")
    private String path;

    /**
     * 将50万用户数据输出到文件
     * @return
     */
    @GetMapping("/initData")
    public Result<String> initData() throws URISyntaxException {
        FileOutputStream outputStream = null;
        try {
            // 在resources目录下创建新文件
            File file = new File(path,"user.csv");
            if (file.exists()) {
                file.delete();
            }
            // 创建文件
            if (file.createNewFile()) {
                log.info("文件已创建: " + file.getName());
            }

            // 向文件中写入内容
            outputStream = new FileOutputStream(file);
            String txt = "";
            Random ageRandom = new Random();
            // 给文件中生产50万条数据
            long beginTime = System.currentTimeMillis();
            log.info("开始时间:【 " + TimeUtil.formatDate(beginTime) + "】");
            int number = 500000;
            for (int i = 1; i <= number; i++) {
                if (i == number) {
                    txt = i + ",zhangsan_" + i + "," + ageRandom.nextInt(100) + ",GD_" + i;
                } else {
                    txt = i + ",zhangsan_" + i + "," + ageRandom.nextInt(100) + ",GD_" + i + "\n";
                }
                outputStream.write(txt.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
            }
            long endTime = System.currentTimeMillis();
            log.info("结束时间:【 " + TimeUtil.formatDate(endTime) + "】");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IoUtil.close(outputStream);
        }
        return Result.createSuccess();
    }

    @GetMapping("/saveTempBatch")
    public Result<String> saveTempBatch(){
        List<User> list = new ArrayList<>();
        User user = new User();
        user.setName("李四");
        user.setAddr("广东省");
        list.add(user);
        userService.saveUserTempBatch(list);
        return Result.createSuccess();
    }
}
