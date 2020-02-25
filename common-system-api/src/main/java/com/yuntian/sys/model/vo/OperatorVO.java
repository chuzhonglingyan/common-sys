package com.yuntian.sys.model.vo;

import com.yuntian.sys.model.entity.Operator;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

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

    private List<String> roles;

    private List<Long> roleIdList;

    private List<String> permissionList;

    private String dept;

    private String sexText;

    private String createName;

    private String updateName;

    public String getSexText() {
        return getSex()==0?"男":"女";
    }
}
