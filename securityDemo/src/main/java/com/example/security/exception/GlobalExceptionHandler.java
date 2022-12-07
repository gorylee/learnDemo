package com.example.security.exception;

import example.common.model.Result;
import example.common.model.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author GoryLee
 * @Date 2022/11/15 20:26
 * @Version 1.0
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = CustomException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handler(CustomException customException){
      log.error("运行时异常--------->:", customException);
      return Result.createError(customException.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handler(MethodArgumentNotValidException e){
        log.error("校验异常--------->:", e);
        ObjectError objectError = e.getBindingResult().getAllErrors().stream().findFirst().orElse(null);
        return Result.createError(ResultCode.ILLEGAL_ARGUMENT.getCode(),objectError.getDefaultMessage());
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handler(IllegalArgumentException e){
        log.error("Assert异常--------->", e);
        return Result.createError(e.getMessage());
    }
}
