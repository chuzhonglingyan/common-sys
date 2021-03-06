package com.yuntian.sys.model.dto;

import com.yuntian.architecture.data.BaseDTO;
import org.hibernate.validator.constraints.Length;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 后台系统-用户表
 * </p>
 *
 * @author yuntian
 * @since 2019-07-06
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LoginDTO extends BaseDTO implements Serializable {

    @NotBlank(message = "账号不能为空,长度必须大于0")
    @Length(max = 50, message = "账号最大50位")
    private String account;


    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 12, message = "密码长度必须在6位到12位之间")
    private String passWord;


}
