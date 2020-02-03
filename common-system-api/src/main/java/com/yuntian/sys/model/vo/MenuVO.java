package com.yuntian.sys.model.vo;


import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.yuntian.sys.model.entity.Menu;

@EqualsAndHashCode(callSuper = true)
@Data
public class MenuVO extends Menu implements Serializable {


    private static final long serialVersionUID = -2302614850186897388L;

    private Boolean isChecked;


}