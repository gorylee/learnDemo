package com.example.springbatch.utils;

import cn.hutool.core.lang.Validator;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description
 * @Author GoryLee
 * @Date 2024/9/14
 */
public class TimeUtil {

    public static String formatDate(long timestamp ) {

        // 创建Date对象
        Date date = new Date(timestamp);
        // 格式化日期输出
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
