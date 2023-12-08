package com.example.blogsystem.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理
 */

@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(Exception.class)
    public ResultAjax doException(Exception e) {
        return ResultAjax.fail(-1, e.getMessage());
    }
}
