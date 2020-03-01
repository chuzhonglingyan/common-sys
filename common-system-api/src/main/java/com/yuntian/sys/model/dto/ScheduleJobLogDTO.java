package com.yuntian.sys.model.dto;

import java.io.Serializable;
import java.util.List;

import com.yuntian.architecture.data.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
*
* <p>
    *  系统-定时任务日志
    * </p>
* @author yuntian
* @since 2020-02-27
*/
@EqualsAndHashCode(callSuper = true)
@Data
public class ScheduleJobLogDTO extends BaseDTO implements Serializable{


    private List<String> createTime;

    /**
     * 任务名称
     */
    private String jobName;

    private Integer status;

    private Long jobId;
}
