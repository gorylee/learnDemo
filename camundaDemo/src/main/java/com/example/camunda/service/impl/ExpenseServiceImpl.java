package com.example.camunda.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.camunda.dao.IExpenseDao;
import com.example.camunda.module.bo.ExpenseQueryBo;
import com.example.camunda.module.entity.Expense;
import com.example.camunda.service.IExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author GoryLee
* @date  2023-6-29
*/
@Service("expenseService")
public class ExpenseServiceImpl extends ServiceImpl<IExpenseDao, Expense> implements IExpenseService {

    @Autowired
    private IExpenseDao dao;

    @Override
    public Expense get(ExpenseQueryBo expenseQuery) {
        return dao.findOne(expenseQuery);
    }

    @Override
    public Page<Expense> list(ExpenseQueryBo expenseQuery) {
        Page<Expense> page = new Page<>(expenseQuery.getPageNo(),expenseQuery.getPageSize());
        List<Expense> expenseList = dao.findList(page,expenseQuery);
        page.setRecords(expenseList);
        return page;
    }

    @Override
    public List<Expense> listAll(ExpenseQueryBo expenseQuery) {
        return dao.findListAll(expenseQuery);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(Expense expense) {
        dao.updateById(expense);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(Expense expense) {
        dao.insert(expense);
    }
}
