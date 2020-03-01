package com.yuntian.sys.quartz;


import com.yuntian.sys.util.SpringBeanUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Administrator
 * @Date: 2019/3/17 0017 19:35
 * @Description:
 */
public class ScheduleTaskBean {

    private Object target;
    private Method method;
    private String params;

    public ScheduleTaskBean(String beanName, String methodName, String params) throws NoSuchMethodException {
        check(beanName, methodName, params);
    }

    private void check(String beanName, String methodName, String params) throws NoSuchMethodException {
        this.target = SpringBeanUtils.getBean(beanName);
        this.params = params;
        if (StringUtils.isNotBlank(params)) {
            this.method = target.getClass().getDeclaredMethod(methodName, String.class);
        } else {
            this.method = target.getClass().getDeclaredMethod(methodName);
        }
    }

    public Long invoke() throws InvocationTargetException, IllegalAccessException {
        long statTime = System.currentTimeMillis();
        ReflectionUtils.makeAccessible(method);
        if (StringUtils.isNotBlank(params)) {
            method.invoke(target, params);
        } else {
            method.invoke(target);
        }
        return System.currentTimeMillis() - statTime;
    }
}
