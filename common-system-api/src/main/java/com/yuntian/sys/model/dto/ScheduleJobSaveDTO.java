package com.yuntian.sys.model.dto;

import com.yuntian.architecture.data.BaseDTO;

import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 系统-定时任务
 * </p>
 *
 * @author yuntian
 * @since 2020-02-27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ScheduleJobSaveDTO extends BaseDTO implements Serializable {

    /**
     * 分组名
     */
    @NotBlank(message = "分组名能为空")
    @Length(max = 200, message = "组名最大100位")
    private String groupName;

    /**
     * 任务名称
     */
    @Length(max = 200, message = "任务名称最大200位")
    @NotBlank(message = "任务名称不能为空")
    private String jobName;

    /**
     * spring bean名称
     */
    @Length(max = 200, message = "执行类最大200位")
    @NotBlank(message = "执行类不能为空")
    private String beanName;

    /**
     * 方法名
     */
    @Length(max = 200, message = "执行方法最大200位")
    @NotBlank(message = "执行方法不能为空")
    private String methodName;

    /**
     * cron表达式
     */
    @Length(max = 100, message = "表达式最大100位")
    @NotBlank(message = "表达式不能为空")
    private String cronExpression;

    /**
     * 参数
     */
    @Length(max = 2000, message = "参数最大2000位")
    private String params;

    @Length(max = 2000, message = "备注最大2000位")
    private String remark;


    private Integer status;

}
