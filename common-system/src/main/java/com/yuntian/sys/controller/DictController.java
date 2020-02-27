package com.yuntian.sys.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.annotation.Resource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yuntian.architecture.data.Result;
import com.yuntian.architecture.data.ResultGenerator;
import com.yuntian.sys.model.entity.Dict;
import com.yuntian.sys.service.DictService;
import com.yuntian.sys.model.dto.DictDTO;

import org.springframework.web.bind.annotation.RestController;
import com.yuntian.sys.common.BaseBackendController;

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
public class DictController extends BaseBackendController {


    @Resource
    private DictService  dictService;


    @PostMapping("/save")
    public Result save(Dict dto) {
        dto.setCreateId(getUserId());
        dto.setUpdateId(getUserId());
        dictService.save(dto);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(Dict dto) {
        dto.setUpdateId(getUserId());
        dictService.deleteByDTO(dto);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(Dict dto) {
        dto.setCreateId(getUserId());
        dto.setUpdateId(getUserId());
        dictService.updateByDTO(dto);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Long id) {
        Dict entity = dictService.getById(id);
        return ResultGenerator.genSuccessResult(entity);
    }


    @PostMapping("/list")
    public IPage<Dict> list(DictDTO dto) {
        return dictService.queryListByPage(dto);
    }

}
