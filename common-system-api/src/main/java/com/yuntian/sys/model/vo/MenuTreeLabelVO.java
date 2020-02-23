package com.yuntian.sys.model.vo;


import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * @Auther: yuntian
 * @Date: 2020/2/1 0001 14:11
 * @Description:
 */
@Data
public class MenuTreeLabelVO implements Serializable {

    private static final long serialVersionUID = 4656867807762635040L;

    private List<MenuTreeLabelVO> children;
    private Long id;
    private Long pid;
    private String label;

}
