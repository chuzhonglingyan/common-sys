package com.yuntian.sys.model.dto;

import com.yuntian.architecture.data.BaseDTO;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
*
* <p>
    *  后台系统-数据字典详情
    * </p>
* @author yuntian
* @since 2020-02-27
*/
@EqualsAndHashCode(callSuper = true)
@Data
public class DictQueyDetailDTO extends BaseDTO implements Serializable{

    private String dictName;

    private String  label;
}
