package com.example.camunda.controller;

import com.example.camunda.module.bo.ExpenseAddBo;
import com.example.camunda.module.bo.ExpenseQueryBo;
import com.example.camunda.module.entity.Expense;
import example.common.entity.JsonResult;
import com.example.camunda.service.IExpenseService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 费用申请表控制器
 * @author GoryLee
 * @date  2023-6-29
 */
@Slf4j
@Api(value = "费用申请表Expense", tags = "费用申请表Expense")
@RestController
@RequestMapping("/expense")
public class ExpenseController {

    @Autowired
    private IExpenseService expenseService;

    @RequestMapping("/submit")
    public JsonResult<?> submit(@RequestBody ExpenseAddBo expenseAddBo){
        Expense expense = new Expense();
        BeanUtils.copyProperties(expenseAddBo,expense);
        expense.setApprovalStatus(1);
        expense.setCreateTime(LocalDateTime.now());
        expenseService.submit(expense);
        return JsonResult.ok();
    }



    @PostMapping("/approval")
    public JsonResult<?> approval(@RequestBody ExpenseQueryBo expenseQueryBo) {
        Expense expense = new Expense();
        BeanUtils.copyProperties(expenseQueryBo,expense);
        expense.setApprovalTime(LocalDateTime.now());
        expenseService.approval(expense,expenseQueryBo);
        return JsonResult.ok();
    }
}
