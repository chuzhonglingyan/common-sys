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
public class RoleUpdateDTO extends BaseDTO implements Serializable {


    private static final long serialVersionUID = 4964008556961057299L;

    @NotNull(message = "id不能为空")
    private Long id;

    @Length(max = 50, message = "角色名称最大50位")
    private String roleName;

}
