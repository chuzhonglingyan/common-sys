package com.yuntian.sys.controller;


import com.yuntian.architecture.data.Result;
import com.yuntian.architecture.data.ResultGenerator;
import com.yuntian.common.BaseSysController;
import com.yuntian.sys.model.dto.DictQueryDTO;
import com.yuntian.sys.model.entity.Dict;
import com.yuntian.sys.annotation.Permission;
import com.yuntian.sys.constant.PermissionCodes;
import com.yuntian.sys.service.DictService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.annotation.Resource;

/**
 * <p>
 * 后台系统-数据字典 前端控制器
 * </p>
 *
 * @author yuntian
 * @since 2020-02-27
 */
@RestController
@RequestMapping("/sys/dict")
public class DictController extends BaseSysController {


    @Resource
    private DictService dictService;


    @Permission(value = PermissionCodes.DICT_ADD)
    @PostMapping("/save")
    public Result save(@RequestBody Dict dto) {
        dto.setCreateId(getUserId());
        dto.setUpdateId(getUserId());
        dictService.saveByDTO(dto);
        return ResultGenerator.genSuccessResult();
    }

    @Permission(value = PermissionCodes.DICT_EDIT)
    @PostMapping("/update")
    public Result update(@RequestBody Dict dto) {
        dto.setUpdateId(getUserId());
        dictService.updateByDTO(dto);
        return ResultGenerator.genSuccessResult();
    }

    @Permission(value = PermissionCodes.DICT_DEL)
    @PostMapping("/delete")
    public Result delete(@RequestBody Dict dto) {
        dto.setUpdateId(getUserId());
        dictService.deleteByDTO(dto);
        return ResultGenerator.genSuccessResult();
    }

    @Permission(value = PermissionCodes.DICT_DEL)
    @PostMapping("/deleteList")
    public Result deleteList(@RequestBody List<Long> idList) {
        dictService.deleteBatchByDTO(getUserId(), idList);
        return ResultGenerator.genSuccessResult();
    }


    @PostMapping("/getInfo")
    public Result getInfo(@RequestParam Long id) {
        Dict entity = dictService.getById(id);
        return ResultGenerator.genSuccessResult(entity);
    }

    @Permission(value = PermissionCodes.DICT_LIST)
    @PostMapping("/list")
    public Result list(@RequestBody DictQueryDTO dto) {
        return ResultGenerator.genSuccessResult(dictService.queryListByPage(dto));
    }

    @PostMapping("/getAll")
    public Result getAll() {
        return ResultGenerator.genSuccessResult(dictService.getAll());
    }

}
