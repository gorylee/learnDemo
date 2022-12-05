package example.common.model;


public enum ResultCode {

    // 胜利返回
    SUCCESS(200, "SUCCESS"),
    // 执行谬误
    ERROR(101, "ERROR"),
    // 参数谬误
    ILLEGAL_ARGUMENT(102, "ILLEGAL_ARGUMENT");

    private int code;
    private String desc;

    ResultCode(int code, String desc) {
        this.setCode(code);
        this.setDesc(desc);
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
