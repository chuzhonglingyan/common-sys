package com.yuntian.sys.model.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 构建前端路由时用到
 * @author Zheng Jie
 * @date 2018-12-20
 */
@Data
public class MenuComponentVo implements Serializable {

    private String name;

    private String path;

    private Boolean hidden;

    private String redirect;

    private String component;

    private Boolean alwaysShow;

    private MenuMetaVo meta;

    private List<MenuComponentVo> children;
}
