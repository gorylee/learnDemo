package com.example.esdemo.model.enums;

/**
 * 搜索排序类型
 */
public enum SortTypeEnum {
    DEFAULT(0,"goodsId"),
    SALECOUNT(1, "saleCount"),
    PRICE(2, "priceMin");

    private Integer key;
    private String value;

    SortTypeEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public static String getValue(Integer key) {
        for (SortTypeEnum c : SortTypeEnum.values()) {
            if (c.getKey().equals(key)) {
                return c.value;
            }
        }
        return null;
    }

    public static String getName(Integer key) {
        for (SortTypeEnum c : SortTypeEnum.values()) {
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
