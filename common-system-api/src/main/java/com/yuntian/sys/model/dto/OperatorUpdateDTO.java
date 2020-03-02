package com.yuntian.sys.model.dto;

import com.yuntian.architecture.data.BaseDTO;
import com.yuntian.valid.SwitchConstraint;

import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.Email;
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
public class OperatorUpdateDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = -8245664076454441606L;

    @NotNull(message = "id不能为空")
    private Long id;

    @Email(message = "邮箱格式错误")
    private String email;

    private String phone;

    private Integer sex;

    @SwitchConstraint(message = "状态值错误")
    private Integer status;

    private List<Long> roleIdList;

}
