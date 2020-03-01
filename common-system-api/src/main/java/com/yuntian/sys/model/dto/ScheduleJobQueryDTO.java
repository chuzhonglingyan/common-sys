package com.yuntian.sys.model.dto;

import com.yuntian.architecture.data.BaseDTO;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
*
* <p>
    * </p>
* @author yuntian
* @since 2020-01-31
*/
@EqualsAndHashCode(callSuper = true)
@Data
public class ScheduleJobQueryDTO extends BaseDTO implements Serializable{

    private static final long serialVersionUID = -293194835878683499L;

    private Integer status;

    private String  blurry;

    private List<String> createTime;


}
