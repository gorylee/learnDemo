package com.example.springbatch.job.writer;

import com.example.springbatch.entity.Sale;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author GoryLee
 * @Date 2024/9/29
 */

public class CustomSaleWriter extends MyBatisBatchItemWriter<Sale> {

    @Override
    public void write(List<? extends Sale> items) {
        super.write(items); // 执行批量插入

        // 获取 StepExecution
        StepExecution stepExecution = StepSynchronizationManager.getContext().getStepExecution();
        if (stepExecution == null) {
            throw new IllegalStateException("No StepExecution found");
        }

        // 获取或初始化 mainIdMap
        Map<String, Long> mainIdMap = (Map<String, Long>) stepExecution.getJobExecution().getExecutionContext().get("mainIdMap");
        if (mainIdMap == null) {
            mainIdMap = new HashMap<>();
        }

        // 填充 mainIdMap
        for (Sale main : items) {
            if (main.getBusinessKey() != null && main.getId() != null) {
                mainIdMap.put(main.getBusinessKey(), main.getId());
            } else {
                throw new IllegalStateException("MainTable must have businessKey and id after insert");
            }
        }

        // 更新 ExecutionContext
        stepExecution.getJobExecution().getExecutionContext().put("mainIdMap", mainIdMap);
    }
}
