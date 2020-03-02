package com.yuntian.sys.model.vo;

import com.yuntian.sys.model.entity.MetaBean;

import java.util.List;
import lombok.Data;

/**
 * @Auther: yuntian
 * @Date: 2020/2/22 0022 22:34
 * @Description:
 */
@Data
public class RouterVO {


    private String name;
    private String path;
    private Boolean hidden;
    private String redirect;
    private String component;
    private Boolean alwaysShow;
    private MetaBean meta;
    private List<RouterVO> children;


}
