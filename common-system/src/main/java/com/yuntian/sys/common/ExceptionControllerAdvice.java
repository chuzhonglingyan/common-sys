package com.yuntian.sys.common;//package com.company.project.exception;

import com.alibaba.fastjson.JSON;
import com.yuntian.architecture.data.Result;
import com.yuntian.architecture.data.ResultCode;
import com.yuntian.architecture.data.ResultGenerator;
import com.yuntian.architecture.data.exception.BusinessException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import lombok.extern.slf4j.Slf4j;


@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {

    /**
     * 拦截捕捉自定义异常 BusinessException.class
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    public Result bussExceptionHandler(BusinessException e) {
        Result result = ResultGenerator.genFailResult(e.getCode(), e.getMessage());
        log.error(JSON.toJSONString(result));
        return result;
    }

    @ExceptionHandler(value = BindException.class)
    public Result handlerException(BindException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            if (StringUtils.isNotBlank(fieldError.getDefaultMessage())) {
                log.error("参数异常：" + fieldError.getDefaultMessage());
                return ResultGenerator.genFailResult(ResultCode.FAIL.code(), fieldError.getDefaultMessage());
            }
        }
        return ResultGenerator.genFailResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器打了个小盹儿~请稍候再试");
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public Result handlerException(HttpMessageNotReadableException ex) {
        return ResultGenerator.genFailResult(ResultCode.FAIL.code(), "请求失败,请传入参数");
    }


    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result handlerException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            if (StringUtils.isNotBlank(fieldError.getDefaultMessage())) {
                log.error("参数异常：" + fieldError.getDefaultMessage());
                return ResultGenerator.genFailResult(ResultCode.FAIL.code(), fieldError.getDefaultMessage());
            }
        }
        return ResultGenerator.genFailResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器打了个小盹儿~请稍候再试");
    }

    @ExceptionHandler({Exception.class})
    public Result handlerException(Exception ex) {
        log.error("发生未知异常：{}", ex);
        return ResultGenerator.genFailResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器打了个小盹儿~请稍候再试");
    }

}
