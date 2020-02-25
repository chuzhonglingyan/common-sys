package com.yuntian.sys.model.dto;

import com.yuntian.architecture.data.BaseDTO;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
*
* <p>
    *  后台系统-用户表
    * </p>
* @author yuntian
* @since 2020-01-31
*/
@EqualsAndHashCode(callSuper = true)
@Data
public class OperatorQueryDTO extends BaseDTO implements Serializable{

    private static final long serialVersionUID = 5407449878947509465L;

    private Integer isEnabled;

    private String  blurry;

    private List<String> createTime;


}
