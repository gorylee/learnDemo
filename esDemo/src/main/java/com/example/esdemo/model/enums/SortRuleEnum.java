package com.example.esdemo.model.enums;

import org.elasticsearch.search.sort.SortOrder;

/**
 * 搜索排序规则
 */
public enum SortRuleEnum {

    ASC(1, SortOrder.ASC),
    DESC(2, SortOrder.DESC);

    private Integer key;
    private SortOrder value;

    SortRuleEnum(Integer key, SortOrder value) {
        this.key = key;
        this.value = value;
    }

    public static SortOrder getValue(Integer key) {
        for (SortRuleEnum c : SortRuleEnum.values()) {
            if (c.getKey().equals(key)) {
                return c.value;
            }
        }
        return null;
    }

    public static String getName(Integer key) {
        for (SortRuleEnum c : SortRuleEnum.values()) {
            if (c.getKey().equals(key)) {
                return c.name();
            }
        }
        return "";
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public SortOrder getValue() {
        return value;
    }

    public void setValue(SortOrder value) {
        this.value = value;
    }

}
