package com.yuntian.sys.common;

import com.yuntian.architecture.data.Result;
import com.yuntian.architecture.data.ResultCode;
import com.yuntian.architecture.data.ResultGenerator;
import com.yuntian.architecture.data.exception.BusinessException;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Objects;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import lombok.extern.slf4j.Slf4j;


/**
 * @author Administrator
 */
@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {


    private static int DUPLICATE_KEY_CODE = 1001;

    /**
     * 拦截捕捉自定义异常 BusinessException.class
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    public Result bussExceptionHandler(BusinessException e) {
        Result result = ResultGenerator.genFailResult(e.getCode(), e.getMessage());
        log.error(e.getMessage(), e);
        return result;
    }


    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public Result handlerException(HttpMessageNotReadableException ex) {
        log.error(ex.getMessage(), ex);
        return ResultGenerator.genFailResult(ResultCode.FAIL.code(), "请求失败,请传入参数");
    }


    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result handlerException(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage(), ex);
        return ResultGenerator.genFailResult(ResultCode.FAIL.code(), Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage());
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public Result handlerException(ConstraintViolationException ex) {
        log.error(ex.getMessage(), ex);
        return ResultGenerator.genFailResult(ResultCode.FAIL.code(), ex.getMessage());
    }

    @ExceptionHandler(value = ValidationException.class)
    public Result handlerException(ValidationException ex) {
        log.error(ex.getMessage(), ex);
        return ResultGenerator.genFailResult(ResultCode.FAIL.code(), ex.getCause().getMessage());
    }


    @ExceptionHandler(NoHandlerFoundException.class)
    public Result handlerException(NoHandlerFoundException ex) {
        log.error(ex.getMessage(), ex);
        return ResultGenerator.genFailResult(ResultCode.NOT_FOUND.code(), "路径不存在，请检查路径是否正确");
    }


    @ExceptionHandler({Exception.class})
    public Result handlerException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ResultGenerator.genFailResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器打了个小盹儿~请稍候再试");
    }


    @ExceptionHandler(DuplicateKeyException.class)
    public Result handleDuplicateKeyException(DuplicateKeyException ex) {
        log.error(ex.getMessage(), ex);
        return ResultGenerator.genFailResult(DUPLICATE_KEY_CODE, "数据重复，请检查后提交");
    }
}
