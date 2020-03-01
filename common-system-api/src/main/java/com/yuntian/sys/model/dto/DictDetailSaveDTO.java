package com.yuntian.sys.model.dto;

import com.yuntian.architecture.data.BaseEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 后台系统-数据字典详情
 * </p>
 *
 * @author yuntian
 * @since 2020-02-27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DictDetailSaveDTO extends BaseEntity {


    /**
     * 字典id
     */
    @NotNull(message = "字典id不能为空")
    private Long dictId;

    /**
     * 字典标签
     */
    @NotBlank(message = "字典标签不能为空")
    private String label;

    /**
     * 字典值
     */
    @NotBlank(message = "字典值不能为空")
    private String value;

    /**
     * 排序
     */
    private Integer sort;

}
