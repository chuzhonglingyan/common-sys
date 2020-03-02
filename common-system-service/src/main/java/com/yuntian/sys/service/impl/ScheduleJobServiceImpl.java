package com.yuntian.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuntian.architecture.data.BaseServiceImpl;
import com.yuntian.architecture.data.exception.BusinessException;
import com.yuntian.architecture.data.util.AssertUtil;
import com.yuntian.architecture.util.BeanCopyUtil;
import com.yuntian.sys.enums.ScheduleStatus;
import com.yuntian.quartz.QzTaskExecutor;
import com.yuntian.quartz.SchedulerUtil;
import com.yuntian.sys.mapper.ScheduleJobMapper;
import com.yuntian.sys.model.dto.ScheduleJobQueryDTO;
import com.yuntian.sys.model.dto.ScheduleJobSaveDTO;
import com.yuntian.sys.model.dto.ScheduleJobUpdateDTO;
import com.yuntian.sys.model.entity.ScheduleJob;
import com.yuntian.sys.service.ScheduleJobService;
import com.yuntian.util.BeanCheckUtil;

import org.quartz.CronExpression;
import org.quartz.SchedulerException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import static com.yuntian.util.BeanCheckUtil.check;

/**
 * <p>
 * 系统-定时任务 服务实现类
 * </p>
 *
 * @author yuntian
 * @since 2020-02-27
 */
@Slf4j
@Service("scheduleJobService")
public class ScheduleJobServiceImpl extends BaseServiceImpl<ScheduleJobMapper, ScheduleJob> implements ScheduleJobService {

    @Resource
    private SchedulerUtil schedulerUtil;

    @Resource
    private QzTaskExecutor qzTaskExecutor;

    @PostConstruct
    public void initConstruct() {
        List<ScheduleJob> scheduleJobList = list();
        if (CollectionUtils.isNotEmpty(scheduleJobList)) {
            scheduleJobList.forEach(scheduleJob -> {
                try {
                    schedulerUtil.start(scheduleJob);
                } catch (SchedulerException e) {
                    log.error(e.getMessage(), e);
                }
            });
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveByDTO(ScheduleJobSaveDTO dto) {
        AssertUtil.isNotNull(dto, "参数不能为空");
        AssertUtil.isNotTrue(CronExpression.isValidExpression(dto.getCronExpression()), "cron表达式不正确");
        check(dto.getBeanName(), dto.getMethodName(), dto.getParams());
        ScheduleJob scheduleJob = BeanCopyUtil.copyProperties(dto, ScheduleJob.class);
        boolean flag = super.save(scheduleJob);
        AssertUtil.isNotTrue(flag, "保存失败,请重试");
        try {
            schedulerUtil.start(scheduleJob);
        } catch (DuplicateKeyException e) {
            BusinessException.throwMessage("实体类和方法唯一约束，不能重复");
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            BusinessException.throwMessage("定时器初始化失败" + e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateByDTO(ScheduleJobUpdateDTO dto) {
        AssertUtil.isNotNull(dto, "参数不能为空");
        AssertUtil.isNotNull(dto.getId(), "id不能为空");
        AssertUtil.isNotTrue(CronExpression.isValidExpression(dto.getCronExpression()), "cron表达式不正确");
        check(dto.getBeanName(), dto.getMethodName(), dto.getParams());
        ScheduleJob scheduleJob = BeanCopyUtil.copyProperties(dto, ScheduleJob.class);
        boolean flag = super.updateById(scheduleJob);
        AssertUtil.isNotTrue(flag, "更新失败,请刷新重试");
        try {
            schedulerUtil.updateTrigger(scheduleJob);
        } catch (SchedulerException | ParseException e) {
            log.error(e.getMessage(), e);
            BusinessException.throwMessage("更新定时任务信息发生异常");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteByDTO(ScheduleJob dto) {
        ScheduleJob scheduleJob = getById(dto.getId());
        if (scheduleJob.getStatus() == ScheduleStatus.NORMAL.getValue()) {
            BusinessException.throwMessage("定时器处于运行状态，无法删除.");
        }
        boolean flag = deleteByIdWithFill(dto);
        AssertUtil.isNotTrue(flag, "删除失败,请刷新重试");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteBatchByDTO(Long operatorId, List<Long> idList) {
        AssertUtil.isNotEmpty(idList, "集合不能为空");
        Collection<ScheduleJob> entityList = new ArrayList<>();
        idList.forEach(id -> {
            ScheduleJob scheduleJobTemp = getById(id);
            if (scheduleJobTemp.getStatus() == ScheduleStatus.NORMAL.getValue()) {
                BusinessException.throwMessage("定时器处于运行状态，无法删除.");
            }
            schedulerUtil.removeJob(scheduleJobTemp);
            ScheduleJob scheduleJob = new ScheduleJob();
            scheduleJob.setId(id);
            scheduleJob.setUpdateId(operatorId);
            entityList.add(scheduleJob);
        });
        boolean flag = deleteByIdsWithFill(entityList);
        AssertUtil.isNotTrue(flag, "删除失败,请刷新重试");

    }


    @Override
    public ScheduleJob getById(Serializable id) {
        AssertUtil.isNotNull(id, "参数不能为空");
        ScheduleJob scheduleJob = super.getById(id);
        AssertUtil.isNotNull(scheduleJob, "该定时器不存在，请刷新页面");
        return scheduleJob;
    }


    @Override
    public IPage<ScheduleJob> queryListByPage(ScheduleJobQueryDTO dto) {
        AssertUtil.isNotNull(dto, "参数不能为空");
        IPage<ScheduleJob> page = new Page<>(dto.getCurrent(), dto.getSize());
        return page(page);
    }

    @Override
    public void exec(ScheduleJob dto) {
        long startTime = System.currentTimeMillis();
        ScheduleJob scheduleJob = getById(dto.getId());
        if (scheduleJob == null) {
            BusinessException.throwMessage("定时器任务不存在");
        }
        AssertUtil.isNotTrue(CronExpression.isValidExpression(scheduleJob.getCronExpression()), "cron表达式不正确");
        BeanCheckUtil.check(scheduleJob.getBeanName(), scheduleJob.getMethodName(), scheduleJob.getParams());
        int result = qzTaskExecutor.executeManual(scheduleJob, startTime, dto.getUpdateId());
        if (result == 0) {
            BusinessException.throwMessage("定时器执行失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void resume(ScheduleJob dto) {
        ScheduleJob scheduleJob = getById(dto.getId());
        check(scheduleJob.getBeanName(), scheduleJob.getMethodName(), scheduleJob.getParams());
        dto.setStatus(ScheduleStatus.NORMAL.getValue());
        LambdaQueryWrapper<ScheduleJob> updateWrapper = new LambdaQueryWrapper<>();
        updateWrapper.eq(ScheduleJob::getId, dto.getId());
        updateWrapper.eq(ScheduleJob::getStatus, ScheduleStatus.PAUSE.getValue());
        boolean flag = update(dto, updateWrapper);
        AssertUtil.isNotTrue(flag, "恢复失败,请刷新页面");
        try {
            schedulerUtil.resume(scheduleJob);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            BusinessException.throwMessage("定时任务恢复异常");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void pause(ScheduleJob dto) {
        ScheduleJob scheduleJob = getById(dto.getId());
        check(scheduleJob.getBeanName(), scheduleJob.getMethodName(), scheduleJob.getParams());
        dto.setStatus(ScheduleStatus.PAUSE.getValue());
        LambdaQueryWrapper<ScheduleJob> updateWrapper = new LambdaQueryWrapper<>();
        updateWrapper.eq(ScheduleJob::getId, dto.getId());
        updateWrapper.eq(ScheduleJob::getStatus, ScheduleStatus.NORMAL.getValue());
        boolean flag = update(dto, updateWrapper);
        AssertUtil.isNotTrue(flag, "暂停失败,请刷新页面");
        try {
            schedulerUtil.pause(scheduleJob);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            BusinessException.throwMessage("定时任务暂停异常");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void changeState(ScheduleJob dto) {
        AssertUtil.isNotNull(dto.getId(), "id不能为空");
        AssertUtil.isNotNull(dto.getStatus(), "状态不能为空");
        if (dto.getStatus() == ScheduleStatus.PAUSE.getValue()) {
            resume(dto);
        } else {
            pause(dto);
        }
    }


}
