package com.yuntian.sys.valid;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * @author Administrator
 * @Auther: yuntian
 * @Date: 2020/2/25 0025 23:21
 * @Description:
 */
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SwitchEnumValidator.class)
@Documented
public @interface SwitchConstraint {

    String message() default "参数值不正确";

    boolean allowEmpty() default true;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
