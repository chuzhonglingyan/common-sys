package com.yuntian.sys.model.dto;

import com.yuntian.architecture.data.BaseDTO;

import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
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
public class MenuSaveDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = -8245664076454441606L;


    @NotNull(message = "父级id不能为空")
    private Long pid;


    @NotBlank(message = "菜单名称不能为空")
    @Length(max = 50, message = "菜单名称最大50位")
    private String name;


    @NotBlank(message = "菜单url不能为空")
    @Length(max = 500, message = "菜单url最大500位")
    private String path;


    @NotBlank(message = "菜单权限code不能为空")
    @Length(max = 500, message = "菜单权限code最大500位")
    private String permissionCode;


    @NotNull(message = "菜单类型不能为空")
    private Integer type;

    private Integer level;

    private Integer sort;

}
