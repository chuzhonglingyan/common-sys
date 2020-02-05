package com.yuntian.sys.aop;


import com.yuntian.architecture.data.exception.BusinessException;
import com.yuntian.sys.annotation.Valid;
import com.yuntian.sys.util.ValidationUtil;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @Auther: yuntian
 * @Date: 2019/10/13 0013 23:31
 * @Description:
 */
@Aspect
@Component
public class ValidAspect {

    @Pointcut("@annotation(com.yuntian.sys.annotation.Valid)")
    public void validPointCut() {

    }

    @Before("validPointCut()")
    public void beforeMethod(JoinPoint joinPoint) {
        Object[] params = joinPoint.getArgs();
        if (params.length == 0) {
            return;
        }
        //获取方法，此处可将signature强转为MethodSignature
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        //参数注解，1维是参数，2维是注解
        Annotation[][] annotations = method.getParameterAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            Object param = params[i];
            Annotation[] paramAnn = annotations[i];
            //参数为空，直接下一个参数
            if (param == null || paramAnn.length == 0) {
                continue;
            }
            for (Annotation annotation : paramAnn) {
                //这里判断当前注解是否为Valid.class
                if (annotation.annotationType().equals(Valid.class)) {
                    //校验该参数，验证一次退出该注解
                    String msg = ValidationUtil.validator(param);
                    if (StringUtils.isNotBlank(msg)) {
                        BusinessException.throwMessage(40, msg);
                    }
                    break;
                }

            }
        }
    }

}
