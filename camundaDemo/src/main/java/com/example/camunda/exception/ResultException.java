package com.example.camunda.exception;

/**
 * 结果异常
 */
public class ResultException extends RuntimeException {

    private Integer status;

    public ResultException(String message) {
        super(message);
    }

    public ResultException(Integer status,String message) {
        super(message);
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
