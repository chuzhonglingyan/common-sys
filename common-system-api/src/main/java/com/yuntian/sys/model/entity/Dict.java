package com.yuntian.sys.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yuntian.architecture.data.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
*
* <p>
*  后台系统-数据字典
* </p>
* @author yuntian
* @since 2020-02-27
*/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_dict")
public class Dict extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
    * 字典名称
    */
    private String name;

    /**
    * 描述
    */
    private String remark;

    /**
    * 是否删除，0-未删除，1-删除，默认为0
    */
    private Integer isDelete;


}
