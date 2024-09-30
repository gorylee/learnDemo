package com.example.springbatch.job.processor;

import com.example.springbatch.entity.SaleDetail;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Description
 * @Author GoryLee
 * @Date 2024/9/29
 */
public class SaleDetailItemProcessor implements ItemProcessor<SaleDetail, SaleDetail> {

    @Override
    public SaleDetail process(SaleDetail item) throws Exception {
        // 获取 StepExecution
        StepExecution stepExecution = StepSynchronizationManager.getContext().getStepExecution();
        if (stepExecution == null) {
            throw new IllegalStateException("No StepExecution found");
        }

        // 获取 JobExecution
        JobExecution jobExecution = stepExecution.getJobExecution();

        // 获取 mainIdMap
        Map<String, Long> mainIdMap = (Map<String, Long>) jobExecution.getExecutionContext().get("mainIdMap");
        if (mainIdMap == null) {
            throw new IllegalStateException("mainIdMap not found in JobExecutionContext");
        }

        // 根据 businessKey 获取 SaleId
        Long mainId = mainIdMap.get(item.getBusinessKey());
        if (mainId == null) {
            throw new IllegalArgumentException("Main ID not found for businessKey: " + item.getBusinessKey());
        }

        // 设置 SaleId 到明细记录
        item.setSaleId(mainId);

        return item;
    }
}
