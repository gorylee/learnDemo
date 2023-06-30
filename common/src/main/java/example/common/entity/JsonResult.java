package example.common.entity;

import example.common.enums.ResultEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

@ApiModel
public class JsonResult<T> {
    /**
     * 响应业务状态
     */
    @ApiModelProperty(value = "1:操作成功;-1:操作失败;401:未登录;403未授权;")
    private Integer status;
    @ApiModelProperty(value = "响应提示")
    private String msg;
    @ApiModelProperty(value = "响应结果")
    private T data;
    public static <T> JsonResult<T> ok(){
        return new JsonResult<>(ResultEnum.SUCCESS.getKey(),"操作成功");
    }
    public static <T> JsonResult<T> ok(Integer status){
        return new JsonResult<>(status,"操作成功");
    }
    public static <T> JsonResult<T> ok(T data){
        return new JsonResult<>(ResultEnum.SUCCESS.getKey(), "操作成功", data);
    }
    public static <T> JsonResult<T> fail(String msg){
        return new JsonResult<>(ResultEnum.FAIL.getKey(),msg);
    }
    public static <T> JsonResult<T> fail(Integer status,String msg){
        return new JsonResult<>(status,msg);
    }
    public static <T> JsonResult<T> fail(){
        return new JsonResult<>(ResultEnum.FAIL.getKey(),"操作失败");
    }
    public static <T> JsonResult<T> fail(ResultEnum resultEnum, String msg){
        return new JsonResult<>(resultEnum.getKey(),msg);
    }
    public static <T> JsonResult<T> fail(ResultEnum resultEnum){
        return new JsonResult<>(resultEnum.getKey(),resultEnum.getValue());
    }
    public static void isNull(Objects obj,String msg){
        if(Objects.isNull(obj)){
            fail(msg);
        }
    }


    public JsonResult(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public JsonResult(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
