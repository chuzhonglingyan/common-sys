package com.yuntian.sys.controller;


import com.yuntian.architecture.data.Result;
import com.yuntian.architecture.data.ResultGenerator;
import com.yuntian.common.BaseSysController;
import com.yuntian.sys.model.dto.ScheduleJobQueryDTO;
import com.yuntian.sys.model.dto.ScheduleJobSaveDTO;
import com.yuntian.sys.model.dto.ScheduleJobUpdateDTO;
import com.yuntian.sys.model.entity.ScheduleJob;
import com.yuntian.sys.service.ScheduleJobService;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.annotation.Resource;

/**
 * <p>
 * 系统-定时任务 前端控制器
 * </p>
 *
 * @author yuntian
 * @since 2020-02-27
 */
@RestController
@RequestMapping("/sys/scheduleJob")
public class ScheduleJobController extends BaseSysController {


    @Resource
    private ScheduleJobService scheduleJobService;


    @PostMapping("/save")
    public Result save(@RequestBody @Validated ScheduleJobSaveDTO dto) {
        dto.setCreateId(getUserId());
        dto.setUpdateId(getUserId());
        scheduleJobService.saveByDTO(dto);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(@RequestBody @Validated ScheduleJobUpdateDTO dto) {
        dto.setUpdateId(getUserId());
        scheduleJobService.updateByDTO(dto);
        return ResultGenerator.genSuccessResult();
    }


    @PostMapping("/delete")
    public Result delete(ScheduleJob dto) {
        dto.setUpdateId(getUserId());
        scheduleJobService.deleteByDTO(dto);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/deleteList")
    public Result deleteList(@RequestBody List<Long> idList) {
        scheduleJobService.deleteBatchByDTO(getUserId(), idList);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/exec")
    public Result exec(@RequestBody ScheduleJob dto) {
        dto.setUpdateId(getUserId());
        scheduleJobService.exec(dto);
        return ResultGenerator.genSuccessResult();
    }


    @PostMapping("/changeState")
    public Result changeState(@RequestBody ScheduleJob dto) {
        dto.setUpdateId(getUserId());
        scheduleJobService.changeState(dto);
        return ResultGenerator.genSuccessResult();
    }


    @PostMapping("/getInfo")
    public Result getInfo(@RequestParam Long id) {
        ScheduleJob entity = scheduleJobService.getById(id);
        return ResultGenerator.genSuccessResult(entity);
    }


    @PostMapping("/list")
    public Result list(@RequestBody ScheduleJobQueryDTO dto) {
        return ResultGenerator.genSuccessResult(scheduleJobService.queryListByPage(dto));
    }

}
