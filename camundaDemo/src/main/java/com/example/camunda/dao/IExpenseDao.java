package com.example.camunda.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.camunda.module.bo.ExpenseQueryBo;
import com.example.camunda.module.entity.Expense;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author GoryLee
* @date  2023-6-29
*/
@Mapper
public interface IExpenseDao extends BaseMapper<Expense> {

    Expense findOne(@Param("query") ExpenseQueryBo expenseQuery);

    List<Expense> findList(@Param("page") Page<Expense> page, @Param("query") ExpenseQueryBo expenseQuery);

    List<Expense> findListAll(@Param("query") ExpenseQueryBo expenseQuery);

}
