package com.example.springbatch.job.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description 分区器设置，把从步骤的from，to，和range 3个设置到从步骤上下文中
 * @Author GoryLee
 * @Date 2024/9/18
 */
@Slf4j
public class DbToDbPartitioner implements Partitioner {
    @Override
    public Map<String, ExecutionContext> partition(int girdSize) {
        String text = "---DbToDbPartitioner--第%s分区-----开始：%s-----结束：%s ---- 数据量：%s------";
        Map<String,ExecutionContext> map = new HashMap<>();
        int from = 1;
        int to = 10000;
        int range = 10000;
        for (int i = 0; i < girdSize; i++) {
            log.info(String.format(text,i,from,to,(to-from+1)));
            ExecutionContext executionContext = new ExecutionContext();
            executionContext.putInt("from",from);
            executionContext.putInt("to",to);
            executionContext.putInt("range",range);
            to += range;
            from +=range;

            map.put("partitioner_"+i,executionContext);

        }
        return map;
    }
}
