package com.lu.schoolproject.commment;

import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobExceptionHandler {


    /**
     * 备用方案，拦截所有的异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public SaResult handleException(Exception e){
        return SaResult.error(e.getMessage());
    }
    //拦截Valid参数校验异常

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public SaResult handleValidException(MethodArgumentNotValidException e) {
        String errorMsg=e.getBindingResult().getFieldError().getDefaultMessage();
        return SaResult.error(errorMsg);
    }
}
