package com.yuntian.sys.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yuntian.architecture.data.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
*
* <p>
*  后台系统-用户角色关系表
* </p>
* @author yuntian
* @since 2020-01-31
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_operator_role")
public class OperatorRole extends BaseEntity {


    private static final long serialVersionUID = -9163803635549591307L;
    /**
    * 用户id
    */
    private Long userId;

    /**
    * 角色id
    */
    private Long roleId;


}
