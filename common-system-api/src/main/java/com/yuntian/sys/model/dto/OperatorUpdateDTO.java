package com.yuntian.sys.model.dto;

import com.yuntian.architecture.data.BaseDTO;

import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

import javax.validation.constraints.Email;
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
public class OperatorUpdateDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = -8245664076454441606L;

    @NotNull(message = "id不能为空")
    private Long id;

    @Length(max = 50, message = "用户名最大50位")
    private String userName;


    @Email(message = "邮箱格式错误")
    private String email;

    private String phone;

    private Integer sex;

    private Integer isEnabled;

}
