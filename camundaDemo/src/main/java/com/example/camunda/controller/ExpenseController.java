package com.example.camunda.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.camunda.module.bo.ExpenseAddBo;
import com.example.camunda.module.bo.ExpenseQueryBo;
import com.example.camunda.module.entity.Expense;
import com.example.camunda.module.vo.JsonResult;
import com.example.camunda.service.IExpenseService;
import com.example.camunda.utils.AssertUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    public JsonResult<?> approval(@RequestBody @Valid  ExpenseQueryBo expenseQueryBo) {
        Expense expense = new Expense();
        BeanUtils.copyProperties(expenseQueryBo,expense);
        expense.setApprovalTime(LocalDateTime.now());
        expenseService.approval(expense,expenseQueryBo);
        return JsonResult.ok();
    }
}
