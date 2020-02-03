package com.yuntian.sys.model.dto;

import com.yuntian.architecture.data.BaseDTO;

import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 后台系统-菜单表
 * </p>
 *
 * @author yuntian
 * @since 2020-01-31
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MenuUpdateDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = -8245664076454441606L;

    @NotNull(message = "id不能为空")
    private Long id;

    @Length(max = 50, message = "菜单名称最大50位")
    private String menuName;


    @Length(max = 500, message = "菜单url最大500位")
    private String menuUrl;

    @Length(max = 500, message = "菜单权限code最大500位")
    private String menuCode;

    private Integer menuType;

    private Integer menuLevel;

    private Integer sort;

}
