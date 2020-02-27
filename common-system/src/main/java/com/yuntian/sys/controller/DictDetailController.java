package com.yuntian.sys.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.annotation.Resource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yuntian.architecture.data.Result;
import com.yuntian.architecture.data.ResultGenerator;
import com.yuntian.sys.model.entity.DictDetail;
import com.yuntian.sys.service.DictDetailService;
import com.yuntian.sys.model.dto.DictDetailDTO;

import org.springframework.web.bind.annotation.RestController;
import com.yuntian.sys.common.BaseBackendController;

/**
 * <p>
 * 后台系统-数据字典详情 前端控制器
 * </p>
 *
 * @author yuntian
 * @since 2020-02-27
 */
@RestController
@RequestMapping("/sys/dict-detail")
public class DictDetailController extends BaseBackendController {


    @Resource
    private DictDetailService  dictDetailService;


    @PostMapping("/save")
    public Result save(DictDetail dto) {
        dto.setCreateId(getUserId());
        dto.setUpdateId(getUserId());
        dictDetailService.save(dto);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(DictDetail dto) {
        dto.setUpdateId(getUserId());
        dictDetailService.deleteByDTO(dto);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(DictDetail dto) {
        dto.setCreateId(getUserId());
        dto.setUpdateId(getUserId());
        dictDetailService.updateByDTO(dto);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Long id) {
        DictDetail entity = dictDetailService.getById(id);
        return ResultGenerator.genSuccessResult(entity);
    }


    @PostMapping("/list")
    public IPage<DictDetail> list(DictDetailDTO dto) {
        return dictDetailService.queryListByPage(dto);
    }

}
