package example.common.enums;

import java.util.LinkedHashMap;
import java.util.Map;

public enum BoolValEnum {

    FALSE(0,"否"),
    TRUE(1,"是");

    private Integer key;
    private String value;
    BoolValEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public static String getValue(Integer key) {
        for (BoolValEnum c : BoolValEnum.values()) {
            if (c.getKey().equals(key)) {
                return c.getValue();
            }
        }
        return null;
    }

    public static Integer getKey(String value){
        for (BoolValEnum c : BoolValEnum.values()) {
            if (c.getValue().equals(value)) {
                return c.getKey();
            }
        }
        return null;
    }

    public static Map<String,String> getOption(){
        Map<String,String> option=new LinkedHashMap<>();
        for (BoolValEnum c : BoolValEnum.values()) {
            option.put(c.getKey().toString(),c.getValue());
        }
        return option;
    }

    public static BoolValEnum getEnum(Integer key){
        for (BoolValEnum c : BoolValEnum.values()) {
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
