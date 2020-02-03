package com.yuntian.sys.model.dto;


import com.yuntian.architecture.data.BaseDTO;

import java.io.Serializable;
import java.util.List;

public class RoleMenuDTO extends BaseDTO implements Serializable {


    private static final long serialVersionUID = 6700164928409280860L;

    private Long roleId;
    private List<Long> menuIdList;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public List<Long> getMenuIdList() {
        return menuIdList;
    }

    public void setMenuIdList(List<Long> menuIdList) {
        this.menuIdList = menuIdList;
    }
}