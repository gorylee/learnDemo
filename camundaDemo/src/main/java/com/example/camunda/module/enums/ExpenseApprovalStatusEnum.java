package com.example.camunda.module.enums;

import java.util.LinkedHashMap;
import java.util.Map;

public enum ExpenseApprovalStatusEnum {
    EXPENSE_UNSUBMIT(0, "待提交"),
    EXPENSE_UNAPPROVAL(1, "待审核"),
    EXPENSE_ADOPTED(2, "审核通过"),
    EXPENSE_REJECT(3, "驳回");

    private Integer key;
    private String value;

    ExpenseApprovalStatusEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public static String getValue(Integer key) {
        for (ExpenseApprovalStatusEnum c : ExpenseApprovalStatusEnum.values()) {
            if (c.getKey().equals(key)) {
                return c.getValue();
            }
        }
        return null;
    }

    public static Integer getKey(String value) {
        for (ExpenseApprovalStatusEnum c : ExpenseApprovalStatusEnum.values()) {
            if (c.getValue().equals(value)) {
                return c.getKey();
            }
        }
        return null;
    }

    public static Map<String, String> getOption() {
        Map<String, String> option = new LinkedHashMap<>();
        for (ExpenseApprovalStatusEnum c : ExpenseApprovalStatusEnum.values()) {
            option.put(c.getKey().toString(), c.getValue());
        }
        return option;
    }

    public static ExpenseApprovalStatusEnum getEnum(Integer key) {
        for (ExpenseApprovalStatusEnum c : ExpenseApprovalStatusEnum.values()) {
            if (c.getKey().equals(key)) {
                return c;
            }
        }
        return null;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
