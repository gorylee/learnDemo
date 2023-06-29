package com.example.camunda.flow.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * flow业务类型
 */
public enum BizFlowTypeEnum {

    EXPENSE_APPROVE("expenseApprove","费用审批");

    private String key;
    private String value;
    BizFlowTypeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static String getValue(String key) {
        for (BizFlowTypeEnum c : BizFlowTypeEnum.values()) {
            if (c.getKey().equals(key)) {
                return c.getValue();
            }
        }
        return null;
    }

    public static String getKey(String value){
        for (BizFlowTypeEnum c : BizFlowTypeEnum.values()) {
            if (c.getValue().equals(value)) {
                return c.getKey();
            }
        }
        return null;
    }

    public static Map<String,String> getOption(){
        Map<String,String> option=new LinkedHashMap<>();
        for (BizFlowTypeEnum c : BizFlowTypeEnum.values()) {
            option.put(c.getKey().toString(),c.getValue());
        }
        return option;
    }

    public static BizFlowTypeEnum getEnum(String key){
        for (BizFlowTypeEnum c : BizFlowTypeEnum.values()) {
            if(c.getKey().equals(key)){
                return c;
            }
        }
        return null;
    }

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

}
