package com.yuntian.sys.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yuntian.architecture.data.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
*
* <p>
*  后台系统-菜单表
* </p>
* @author yuntian
* @since 2020-01-31
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_menu")
public class Menu extends BaseEntity {


    private static final long serialVersionUID = -4651342345937174534L;
    /**
    * 父菜单id
    */
    private Long pid;

    /**
    * 菜单名称
    */
    private String name;

    /**
    * 菜单等级 1一级菜单  2 二级菜单  3 三级菜单
    */
    private Integer level;

    /**
    * 菜单路径
    */
    private String path;

    /**
    * 授权(多个用逗号分隔，如：user:list,user:add)
    */
    private String permission;

    /**
    * 菜单类型，0：根目录,1：菜单，2：操作
    */
    private Integer type;

    /**
    * 0：禁用  1：启用
    */
    private Integer status;


    /**
     * 0：隐藏  1：可见
     */
    private Integer visible;

    /**
    * 排序
    */
    private Integer sort;


    private String component;


    private String componentName;

    private String icon;

    private Integer cache;

    private Integer isLinked;

    /**
    * 是否删除，0-未删除，1-删除，默认为0
    */
    private Integer isDelete;


}
