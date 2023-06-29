package com.example.camunda.module.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 接口结果枚举
 */
public enum ResultEnum {
    SUCCESS(1,"操作成功"),
    FAIL(-1,"操作失败"),
	NO_LOGIN(-100,"未登陆"),

	FORBIDDEN(-200,"您没有权限"),
    SYSTEM_ERROR(555,"系统异常");

	private Integer key;
    private String value;
    ResultEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }
    
    public static String getValue(Integer key) {
        for (ResultEnum c : ResultEnum.values()) {
            if (c.getKey().equals(key)) {
                return c.getValue();
            }
        }
        return null;
    }
    
    public static Integer getKey(String value){
    	for (ResultEnum c : ResultEnum.values()) {
            if (c.getValue().equals(value)) {
                return c.getKey();
            }
        }
        return null;
    }

    public static Map<String,String> getOption(){
        Map<String,String> option=new LinkedHashMap<>();
        for (ResultEnum c : ResultEnum.values()) {
            option.put(c.getKey().toString(),c.getValue());
        }
        return option;
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
