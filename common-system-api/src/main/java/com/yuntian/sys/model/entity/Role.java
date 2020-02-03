package com.yuntian.sys.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yuntian.architecture.data.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
*
* <p>
*  后台系统-角色表
* </p>
* @author yuntian
* @since 2020-01-31
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_role")
public class Role extends BaseEntity {


    private static final long serialVersionUID = 378403621631306392L;

    /**
    * 角色key
    */
    private String roleKey;

    /**
    * 角色名称
    */
    private String name;

    /**
    * 角色状态 0:冻结  1：开通
    */
    private Integer status;

    /**
    * 描述
    */
    private String description;

    /**
    * 是否删除，0-未删除，1-删除，默认为0
    */
    private Integer isDelete;


}
