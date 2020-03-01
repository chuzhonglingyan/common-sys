package com.yuntian.sys.model.dto;

import java.io.Serializable;
import com.yuntian.architecture.data.BaseDTO;

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
public class DictDetailDTO extends BaseDTO implements Serializable{

    private Long dictId;
}
