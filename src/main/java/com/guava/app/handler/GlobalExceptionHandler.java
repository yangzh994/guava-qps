package com.guava.app.handler;

import com.guava.app.common.Result;
import com.guava.app.exception.BizException;
import com.guava.app.exception.QPSException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = QPSException.class)
    public Result QPSException(Exception exp) {
        return new Result(8888, exp.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = BizException.class)
    public Result BizException(Exception exp) {
        return new Result(9999, exp.getMessage());
    }
}
