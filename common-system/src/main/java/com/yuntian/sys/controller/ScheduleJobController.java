package com.yuntian.sys.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.annotation.Resource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yuntian.architecture.data.Result;
import com.yuntian.architecture.data.ResultGenerator;
import com.yuntian.sys.model.entity.ScheduleJob;
import com.yuntian.sys.service.ScheduleJobService;
import com.yuntian.sys.model.dto.ScheduleJobDTO;

import org.springframework.web.bind.annotation.RestController;
import com.yuntian.sys.common.BaseBackendController;

/**
 * <p>
 * 系统-定时任务 前端控制器
 * </p>
 *
 * @author yuntian
 * @since 2020-02-27
 */
@RestController
@RequestMapping("/sys/schedule-job")
public class ScheduleJobController extends BaseBackendController {


    @Resource
    private ScheduleJobService  scheduleJobService;


    @PostMapping("/save")
    public Result save(ScheduleJob dto) {
        dto.setCreateId(getUserId());
        dto.setUpdateId(getUserId());
        scheduleJobService.save(dto);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(ScheduleJob dto) {
        dto.setUpdateId(getUserId());
        scheduleJobService.deleteByDTO(dto);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(ScheduleJob dto) {
        dto.setCreateId(getUserId());
        dto.setUpdateId(getUserId());
        scheduleJobService.updateByDTO(dto);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Long id) {
        ScheduleJob entity = scheduleJobService.getById(id);
        return ResultGenerator.genSuccessResult(entity);
    }


    @PostMapping("/list")
    public IPage<ScheduleJob> list(ScheduleJobDTO dto) {
        return scheduleJobService.queryListByPage(dto);
    }

}
