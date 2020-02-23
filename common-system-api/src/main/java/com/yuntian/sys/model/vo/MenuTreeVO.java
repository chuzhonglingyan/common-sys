package com.yuntian.sys.model.vo;


import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.yuntian.sys.model.entity.Menu;

/**
 * @Auther: yuntian
 * @Date: 2020/2/1 0001 14:11
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MenuTreeVO extends Menu implements Serializable {

    private static final long serialVersionUID = -2694666422345560104L;

    private List<MenuTreeVO> children;
}
