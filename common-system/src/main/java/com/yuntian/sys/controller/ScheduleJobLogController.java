package com.yuntian.sys.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.annotation.Resource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yuntian.architecture.data.Result;
import com.yuntian.architecture.data.ResultGenerator;
import com.yuntian.sys.model.entity.ScheduleJobLog;
import com.yuntian.sys.service.ScheduleJobLogService;
import com.yuntian.sys.model.dto.ScheduleJobLogDTO;

import org.springframework.web.bind.annotation.RestController;
import com.yuntian.sys.common.BaseBackendController;

/**
 * <p>
 * 系统-定时任务日志 前端控制器
 * </p>
 *
 * @author yuntian
 * @since 2020-02-27
 */
@RestController
@RequestMapping("/sys/schedule-job-log")
public class ScheduleJobLogController extends BaseBackendController {


    @Resource
    private ScheduleJobLogService  scheduleJobLogService;


    @PostMapping("/save")
    public Result save(ScheduleJobLog dto) {
        dto.setCreateId(getUserId());
        dto.setUpdateId(getUserId());
        scheduleJobLogService.save(dto);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(ScheduleJobLog dto) {
        dto.setUpdateId(getUserId());
        scheduleJobLogService.deleteByDTO(dto);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(ScheduleJobLog dto) {
        dto.setCreateId(getUserId());
        dto.setUpdateId(getUserId());
        scheduleJobLogService.updateByDTO(dto);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Long id) {
        ScheduleJobLog entity = scheduleJobLogService.getById(id);
        return ResultGenerator.genSuccessResult(entity);
    }


    @PostMapping("/list")
    public IPage<ScheduleJobLog> list(ScheduleJobLogDTO dto) {
        return scheduleJobLogService.queryListByPage(dto);
    }

}
