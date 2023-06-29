package com.example.camunda.controller;

import com.example.camunda.module.bo.ExpenseQueryBo;
import com.example.camunda.module.entity.Expense;
import com.example.camunda.module.vo.JsonResult;
import com.example.camunda.service.IExpenseService;
import com.example.camunda.utils.AssertUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation(value = "查询费用申请表", notes = "查询费用申请表")
    @ApiOperationSupport(author = "GoryLee")
    @GetMapping("/get")
    //@Permission("fi:expense:get")
    public JsonResult<Expense> get(ExpenseQueryBo expenseQuery){
        Expense expense = expenseService.get(expenseQuery);
        AssertUtils.isNull(expense,"未查询到费用申请表");
        return JsonResult.ok(expense);
    }

}
