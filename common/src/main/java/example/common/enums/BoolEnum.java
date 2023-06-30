package example.common.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * boolean enum
 */
public enum BoolEnum {

    FALSE(0,"false"),
    TRUE(1,"true");

	private Integer key;
    private String value;
    BoolEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }
    
    public static String getValue(Integer key) {
        for (BoolEnum c : BoolEnum.values()) {
            if (c.getKey().equals(key)) {
                return c.getValue();
            }
        }
        return null;
    }
    
    public static Integer getKey(String value){
    	for (BoolEnum c : BoolEnum.values()) {
            if (c.getValue().equals(value)) {
                return c.getKey();
            }
        }
        return null;
    }

    public static Map<String,String> getOption(){
        Map<String,String> option=new LinkedHashMap<>();
        for (BoolEnum c : BoolEnum.values()) {
            option.put(c.getKey().toString(),c.getValue());
        }
        return option;
    }

    public static BoolEnum getEnum(Integer key){
        for (BoolEnum c : BoolEnum.values()) {
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
