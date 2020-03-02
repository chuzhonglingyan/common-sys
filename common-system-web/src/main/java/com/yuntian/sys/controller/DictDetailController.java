package com.yuntian.sys.controller;


import com.yuntian.architecture.data.Result;
import com.yuntian.architecture.data.ResultGenerator;
import com.yuntian.common.BaseSysController;
import com.yuntian.sys.model.dto.DictDetailSaveDTO;
import com.yuntian.sys.model.dto.DictDetailUpdateDTO;
import com.yuntian.sys.model.dto.DictQueyDetailDTO;
import com.yuntian.sys.model.entity.DictDetail;
import com.yuntian.sys.annotation.Permission;
import com.yuntian.sys.constant.PermissionCodes;
import com.yuntian.sys.service.DictDetailService;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.annotation.Resource;

/**
 * <p>
 * 后台系统-数据字典详情 前端控制器
 * </p>
 *
 * @author yuntian
 * @since 2020-02-27
 */
@RestController
@RequestMapping("/sys/dictDetail")
public class DictDetailController extends BaseSysController {


    @Resource
    private DictDetailService dictDetailService;

    @Permission(value = PermissionCodes.DICT_ADD)
    @PostMapping("/save")
    public Result save(@RequestBody @Validated DictDetailSaveDTO dto) {
        dto.setCreateId(getUserId());
        dto.setUpdateId(getUserId());
        dictDetailService.saveByDTO(dto);
        return ResultGenerator.genSuccessResult();
    }

    @Permission(value = PermissionCodes.DICT_EDIT)
    @PostMapping("/update")
    public Result update(@RequestBody @Validated DictDetailUpdateDTO dto) {
        dto.setUpdateId(getUserId());
        dictDetailService.updateByDTO(dto);
        return ResultGenerator.genSuccessResult();
    }

    @Permission(value = PermissionCodes.DICT_DEL)
    @PostMapping("/delete")
    public Result delete(@RequestBody DictDetail dto) {
        dto.setUpdateId(getUserId());
        dictDetailService.deleteByDTO(dto);
        return ResultGenerator.genSuccessResult();
    }

    @Permission(value = PermissionCodes.DICT_DEL)
    @PostMapping("/deleteList")
    public Result deleteList(@RequestBody List<Long> idList) {
        dictDetailService.deleteBatchByDTO(getUserId(), idList);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/getInfo")
    public Result getInfo(@RequestBody DictDetail dto) {
        DictDetail entity = dictDetailService.getById(dto.getId());
        return ResultGenerator.genSuccessResult(entity);
    }


    @PostMapping("/list")
    public Result list(@RequestBody DictQueyDetailDTO dto) {
        return ResultGenerator.genSuccessResult(dictDetailService.queryListByPage(dto));
    }

}
