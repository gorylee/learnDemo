package example.common.exception;


import example.common.entity.JsonResult;
import example.common.enums.ResultEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理类
 */
@RestControllerAdvice
public class BaseExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(BaseExceptionHandler.class);

    /**
     * 自定义抛出异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = ResultException.class)
    public JsonResult resultExceptionHandler(ResultException e){
        if(e.getStatus()!=null){
            return JsonResult.fail(e.getStatus(),e.getMessage());
        }else {
            return JsonResult.fail(ResultEnum.FAIL,e.getMessage());
        }
    }

    /**
     * 数据校验异常
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public JsonResult validExceptionHandler(MethodArgumentNotValidException e){
        logger.warn(e.getMessage());
        return JsonResult.fail(ResultEnum.FAIL,e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(value = BindException.class)
    public JsonResult validExceptionHandler(BindException e){
        logger.warn(e.getMessage());
        return JsonResult.fail(ResultEnum.FAIL,e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    //*
    // * 其他异常
    // * @param e
    // * @return
    //
    //@ExceptionHandler(value = Exception.class)
    //public JsonResult otherExceptionHandler(Exception e){
    //    return JsonResult.fail(ResultEnum.FAIL,"网络异常");
    //}

}
