package com.common;

import com.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public R handleBusinessException(BusinessException e) {
        logger.error("业务异常: {}", e.getMessage(), e);
        return R.error(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public R handleNoResourceFoundException(NoResourceFoundException e) {
        logger.warn("资源不存在: {}", e.getResourcePath());
        return R.error(404, "资源不存在");
    }

    @ExceptionHandler(Exception.class)
    public R handleException(Exception e) {
        logger.error("系统异常: {}", e.getMessage(), e);
        return R.error(ResultCode.ERROR.getCode(), ResultCode.ERROR.getMsg());
    }
}
