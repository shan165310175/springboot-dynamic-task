package com.daily.taskcenter.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

/**
 * @author z
 * @date 2019/7/26 10:40
 **/
@RestControllerAdvice
public class ExceptionHandler {

    @Data
    public static class ExceptionInfo {
        Integer errCode = 0;
        String msg;
        Date timestamp = new Date();

        ExceptionInfo(String msg) {
            this.msg = msg;
        }

        ExceptionInfo(String msg, int code) {
            this.msg = msg;
            this.errCode = code;
        }
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ServiceException.class)
    public Object excpetion(ServiceException e) {
        int errCode = e.getErrCode();
        return new ExceptionInfo(e.getMessage(), errCode);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Throwable.class)
    public Object throwable(Throwable e) {
        return new ExceptionInfo(e.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

}
