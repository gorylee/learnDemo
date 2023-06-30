package com.example.camunda.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.camunda.dao.IExpenseDao;
import com.example.camunda.flow.enums.BizFlowTypeEnum;
import com.example.camunda.flow.service.FlowUtil;
import com.example.camunda.module.bo.ExpenseQueryBo;
import com.example.camunda.module.bo.UserQueryBo;
import com.example.camunda.module.entity.Expense;
import com.example.camunda.module.entity.User;
import com.example.camunda.service.IExpenseService;
import com.example.camunda.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author GoryLee
* @date  2023-6-29
*/
@Service("expenseService")
public class ExpenseServiceImpl extends ServiceImpl<IExpenseDao, Expense> implements IExpenseService {

    @Autowired
    private IExpenseDao dao;

    @Autowired
    private IUserService userService;

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
    public void submit(Expense expense) {
        dao.insert(expense);
        List<User> userList = userService.listAll(new UserQueryBo());
        Set<String> companyUsers = userList.stream().filter(x -> x.getUserRole() == 1)
                .map(item -> item.getId().toString()).collect(Collectors.toSet());
        Set<String> groupUsers = userList.stream().filter(x -> x.getUserRole() == 2)
                .map(item -> item.getId().toString()).collect(Collectors.toSet());
        Set<String> headUsers = userList.stream().filter(x -> x.getUserRole() == 3)
                .map(item -> item.getId().toString()).collect(Collectors.toSet());

        Map<String,Object> params = new HashMap<>();
        params.put("companyUsers", companyUsers);
        params.put("groupUsers",groupUsers);
        params.put("headUsers",headUsers);
        FlowUtil.startProcess(BizFlowTypeEnum.EXPENSE_APPROVE.getKey(),expense.getId(),params);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approval(Expense expense, ExpenseQueryBo expenseQueryBo) {
        if(expenseQueryBo.getApprovalStatus() == 2){
            //通过
            if(FlowUtil.claimAndCompleteTask(BizFlowTypeEnum.EXPENSE_APPROVE.getKey(),expense.getId().toString(),expenseQueryBo.getApprovalId().toString())){
                expense.setApprovalStatus(2);
            }else {
                expense.setApprovalStatus(1);
            }
        }else {
            //驳回
            FlowUtil.destroyProcess(BizFlowTypeEnum.EXPENSE_APPROVE.getKey(),expense.getId().toString(),"不想通过",expenseQueryBo.getApprovalId().toString());
            expense.setApprovalStatus(3);
        }

        this.updateById(expense);
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
