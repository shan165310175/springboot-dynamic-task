package com.daily.taskcenter.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * 服务异常
 *
 * @author z
 * @date 2019/7/25 14:10
 **/
public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = -1318947966974276849L;

    @Getter
    @Setter
    private int errCode = HttpStatus.BAD_REQUEST.value();

    public ServiceException(String msg) {
        super(msg);
    }

    public ServiceException(String msg, int errCode) {
        this(msg);
        this.errCode = errCode;
    }
}
