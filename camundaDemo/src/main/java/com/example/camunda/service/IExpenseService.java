package com.example.camunda.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.camunda.module.bo.ExpenseQueryBo;
import com.example.camunda.module.entity.Expense;

import java.util.List;

/**
* @author GoryLee
* @date  2023-6-29
*/
public interface IExpenseService extends IService<Expense> {
    /**
    * 新增费用申请表
    */
    void add(Expense expense);

    /**
    * 查询费用申请表详情
    */
    Expense get(ExpenseQueryBo expenseQuery);

    /**
    * 编辑费用申请表
    */
    void edit(Expense expense);

    /**
    * 分页查询费用申请表
    */
    Page<Expense> list(ExpenseQueryBo expenseQuery);

    /**
    * 查询所有费用申请表
    */
    List<Expense> listAll(ExpenseQueryBo expenseQuery);
}
