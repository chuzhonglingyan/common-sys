package com.yuntian.sys.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yuntian.architecture.data.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
*
* <p>
*  系统-定时任务
* </p>
* @author yuntian
* @since 2020-02-27
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_schedule_job")
public class ScheduleJob extends BaseEntity {

    private static final long serialVersionUID = 1L;

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
    * cron表达式
    */
    private String cronExpression;

    /**
    * 分组名
    */
    private String groupName;

    /**
    * 任务状态  1：正常  0：暂停
    */
    private Integer status;

    /**
    * 备注
    */
    private String remark;

    /**
    * 是否删除，0-未删除，1-删除，默认为0
    */
    private Integer isDelete;


}
