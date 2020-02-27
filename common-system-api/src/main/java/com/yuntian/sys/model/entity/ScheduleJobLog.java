package com.yuntian.sys.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yuntian.architecture.data.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
*
* <p>
*  系统-定时任务日志
* </p>
* @author yuntian
* @since 2020-02-27
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_schedule_job_log")
public class ScheduleJobLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
    * 任务id
    */
    private Long jobId;

    /**
    * 任务名称
    */
    private String jobName;

    /**
    * spring bean名称
    */
    private String beanName;

    /**
    * 方法名
    */
    private String methodName;

    /**
    * 参数
    */
    private String params;

    /**
    * 任务状态 0：失败 1：成功 
    */
    private Integer status;

    /**
    * 失败信息
    */
    private String error;

    /**
    * 执行机器
    */
    private String ip;

    /**
    * 耗时(单位：毫秒)
    */
    private Long times;


}
