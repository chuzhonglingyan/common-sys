package com.yuntian.sys.model.vo;

import com.yuntian.sys.model.entity.Dict;
import com.yuntian.sys.model.entity.DictDetail;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Auther: yuntian
 * @Date: 2020/2/29 0029 13:49
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DictVO extends Dict implements Serializable {

    private List<DictDetail> dictDetails;
}
