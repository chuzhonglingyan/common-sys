package com.yuntian.sys.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

/**
 * @Auther: yuntian
 * @Date: 2019/10/14 0014 20:27
 * @Description:
 */
public class ValidationUtil {

    private static Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }


    public static <T> String validator(T t) {
        Set<ConstraintViolation<T>> set = validator.validate(t);
        for (ConstraintViolation<T> constraintViolation : set) {
            if (StringUtils.isNotBlank(constraintViolation.getMessage())) {
                return constraintViolation.getMessage();
            }
        }
        return "";
    }

}
