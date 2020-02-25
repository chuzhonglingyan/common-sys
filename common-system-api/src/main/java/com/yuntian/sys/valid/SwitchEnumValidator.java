package com.yuntian.sys.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Administrator
 * @Auther: yuntian
 * @Date: 2020/2/25 0025 23:21
 * @Description:
 */
public class SwitchEnumValidator implements ConstraintValidator<SwitchConstraint, Integer> {

    private  SwitchConstraint switchConstraint;

    @Override
    public void initialize(SwitchConstraint arg0) {
        switchConstraint=arg0;
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext arg1) {
        if (switchConstraint.allowEmpty()){
            value=0;
        }
        return SwitchEnum.isInEnum(value);
    }

}
