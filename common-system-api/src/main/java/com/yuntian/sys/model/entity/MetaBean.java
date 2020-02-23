package com.yuntian.sys.model.entity;

import lombok.Data;

/**
 * @Auther: yuntian
 * @Date: 2020/2/22 0022 22:39
 * @Description:
 */
@Data
public class MetaBean {

    /**
     * title : 系统管理
     * icon : system
     * noCache : true
     */

    private String title;
    private String icon;
    private boolean noCache;
}
