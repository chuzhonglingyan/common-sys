package com.yuntian.sys.model.vo;


import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.yuntian.sys.model.entity.Role;

@EqualsAndHashCode(callSuper = true)
@Data
public class RoleVO extends Role implements Serializable {

    private static final long serialVersionUID = -1474181370135140579L;

    private Boolean isChecked;

    private List<Long> menuIdList;


}