package com.yuntian.sys.model.dto;

import java.io.Serializable;
import java.util.Map;

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
public class DownQueryDTO extends OperatorQueryDTO implements Serializable{

    private static final long serialVersionUID = 3825012122748930340L;
    private Map<String,String> fieldMap;

}
