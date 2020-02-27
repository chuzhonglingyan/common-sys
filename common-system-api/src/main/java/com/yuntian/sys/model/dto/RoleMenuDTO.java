package com.yuntian.sys.model.dto;


import com.yuntian.architecture.data.BaseDTO;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RoleMenuDTO extends BaseDTO implements Serializable {


    private static final long serialVersionUID = 6700164928409280860L;

    private Long roleId;
    private List<Long> menuIdList;


}