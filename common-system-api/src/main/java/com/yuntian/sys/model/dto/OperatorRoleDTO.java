package com.yuntian.sys.model.dto;

import com.yuntian.architecture.data.BaseDTO;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: yuntian
 * @Date: 2020/2/1 0001 18:48
 * @Description:
 */
public class OperatorRoleDTO extends BaseDTO implements Serializable {


    private static final long serialVersionUID = 6700164928409280860L;
    private Long operaterId;
    private List<Long> roleList;

    public Long getOperaterId() {
        return operaterId;
    }

    public void setOperaterId(Long operaterId) {
        this.operaterId = operaterId;
    }

    public List<Long> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Long> roleList) {
        this.roleList = roleList;
    }
}