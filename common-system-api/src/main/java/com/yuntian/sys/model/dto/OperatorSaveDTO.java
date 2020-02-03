package com.yuntian.sys.model.dto;

import com.yuntian.architecture.data.BaseDTO;

import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

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
public class OperatorSaveDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = -8245664076454441606L;


    @NotBlank(message = "账号不能为空")
    @Length(max = 50, message = "账号最大50位")
    private String account;


    @NotBlank(message = "用户名不能为空")
    @Length(max = 50, message = "用户名最大50位")
    private String userName;

    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 12, message = "密码长度必须在6位到12位之间")
    private String passWord;


    @NotBlank(message = "邮箱格式错误")
    @Email(message = "邮箱格式错误")
    private String email;


    private String phone;


}
