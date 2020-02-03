package com.yuntian.sys.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.yuntian.sys.model.entity.Operator;

/**
 * @Auther: yuntian
 * @Date: 2020/2/1 0001 23:10
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OperatorVO extends Operator {

    /**
     * token
     */
    private String token;

    private String roles;

}
