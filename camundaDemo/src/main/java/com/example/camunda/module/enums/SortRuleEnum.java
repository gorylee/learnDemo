package com.example.camunda.module.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *  排序规则
 */
public enum SortRuleEnum {
    ASC(1,"ASC"),
    DESC(2,"DESC");
	private Integer key;
    @EnumValue
    private String value;
    SortRuleEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }
    
    public static String getValue(Integer key) {
        for (SortRuleEnum c : SortRuleEnum.values()) {
            if (c.getKey().equals(key)) {
                return c.getValue();
            }
        }
        return null;
    }
    
    public static Integer getKey(String value){
    	for (SortRuleEnum c : SortRuleEnum.values()) {
            if (c.getValue().equals(value)) {
                return c.getKey();
            }
        }
        return null;
    }

    public static Map<String,String> getOption(){
        Map<String,String> option=new LinkedHashMap<>();
        for (SortRuleEnum c : SortRuleEnum.values()) {
            option.put(c.getKey().toString(),c.getValue());
        }
        return option;
    }

    public static SortRuleEnum getEnum(Integer key){
        for (SortRuleEnum c : SortRuleEnum.values()) {
            if(c.getKey().equals(key)){
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
