package com.yuntian.util;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.yuntian.architecture.data.exception.BusinessException;
import com.yuntian.architecture.util.SpringBeanUtils;

/**
 * @Auther: yuntian
 * @Date: 2020/3/1 0001 12:49
 * @Description:
 */
public class BeanCheckUtil {

    public static void check(String beanName, String methodName, String params) {
        Object target = null;
        try {
            target = SpringBeanUtils.getBean(beanName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (target == null) {
            BusinessException.throwMessage("没有该任务类");
        }
        if (StringUtils.isNotBlank(params)) {
            try {
                target.getClass().getDeclaredMethod(methodName, String.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                BusinessException.throwMessage("没有该方法");
            }
        } else {
            try {
                target.getClass().getDeclaredMethod(methodName);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                BusinessException.throwMessage("没有该方法");
            }
        }
    }
}
