package example.common.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@Builder
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code;
    private String msg;
    private T data;

    /**
     * 构造方法
     */
    private Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    private Result(int code, T data) {
        this.code = code;
        this.data = data;
    }
    private Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    private Result(int code) {
        this.code = code;
    }

    /**
     * 申请胜利
     * @param msg  返回信息
     * @param data 泛型数据
     * @param <T>  返回数据，能够不填
     * @return 1.状态码（默认） 2.返回信息 3.泛型数据
     */
    public static <T> Result<T> createSuccess(String msg, T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), msg, data);
    }
    public static <T> Result<T> createSuccess(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), data);
    }
    public static <T> Result<T> createSuccess(String msg) {
        return new Result<>(ResultCode.SUCCESS.getCode(), msg);
    }
    public static <T> Result<T> createSuccess() {
        return new Result<>(ResultCode.SUCCESS.getCode());
    }

    /**
     * 申请失败
     * @param code
     * @param msg
     * @return 1.状态码（自定义） 2.返回信息（自定义）
     */
    public static <T> Result<T> createError(int code, String msg) {
        return new Result<>(code, msg);
    }
    public static <T> Result<T> createError() {
        return new Result<>(ResultCode.ERROR.getCode(), ResultCode.ERROR.getDesc());
    }
    public static <T> Result<T> createError(String msg) {
        return new Result<>(ResultCode.ERROR.getCode(), msg);
    }

}
