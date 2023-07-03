package com.example.security.exception;

import lombok.Data;

/**
 * @Author GoryLee
 * @Date 2022/12/7 21:40
 * @Version 1.0
 */
@Data
public class CustomException extends RuntimeException{

    private String message;

    public CustomException(String message) {
        super(message);
        this.message = message;
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

}
