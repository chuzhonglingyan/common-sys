package com.yuntian.sys.model.vo;


import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.yuntian.architecture.data.ITree;
import com.yuntian.sys.model.entity.Menu;

/**
 * @author yuntian
 * @date 2020/3/4 0004 22:52
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MenuTreeVO extends Menu implements Serializable, ITree<MenuTreeVO> {

    private static final long serialVersionUID = -2694666422345560104L;

    private List<MenuTreeVO> children;
}
