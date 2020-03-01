package com.yuntian.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuntian.architecture.data.BaseServiceImpl;
import com.yuntian.architecture.data.exception.BusinessException;
import com.yuntian.architecture.data.util.AssertUtil;
import com.yuntian.sys.mapper.ScheduleJobLogMapper;
import com.yuntian.sys.model.dto.ScheduleJobLogDTO;
import com.yuntian.sys.model.entity.DictDetail;
import com.yuntian.sys.model.entity.Operator;
import com.yuntian.sys.model.entity.ScheduleJobLog;
import com.yuntian.sys.service.ScheduleJobLogService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 系统-定时任务日志 服务实现类
 * </p>
 *
 * @author yuntian
 * @since 2020-02-27
 */
@Service("scheduleJobLogService")
public class ScheduleJobLogServiceImpl extends BaseServiceImpl<ScheduleJobLogMapper, ScheduleJobLog> implements ScheduleJobLogService {


    @Override
    public ScheduleJobLog getById(Serializable id) {
        return super.getById(id);
    }


    @Override
    public void saveByDTO(ScheduleJobLog dto) {
        AssertUtil.isNotNull(dto.getJobId(), "任务id不能为空");
        AssertUtil.isNotBlank(dto.getIp(), "ip不能为空");
        AssertUtil.isNotBlank(dto.getBeanName(), "实体类不能为空");
        AssertUtil.isNotBlank(dto.getMethodName(), "方法不能为空");
        super.save(dto);
    }


    @Override
    public void updateByDTO(ScheduleJobLog dto) {
        AssertUtil.isNotNull(dto, "参数不能为空");
        AssertUtil.isNotNull(dto.getId(), "id不能为空");
        boolean flag = super.updateById(dto);
        if (!flag) {
            BusinessException.throwMessage("更新失败,请刷新重试");
        }
    }


    @Override
    public void deleteByDTO(ScheduleJobLog dto) {
        AssertUtil.isNotNull(dto, "参数不能为空");
        AssertUtil.isNotNull(dto.getId(), "id不能为空");
        boolean flag = deleteByIdWithFill(dto);
        if (!flag) {
            BusinessException.throwMessage("删除失败,请刷新重试");
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateBatchById(Collection<ScheduleJobLog> entityList) {
        AssertUtil.isNotEmpty(entityList, "参数不能为空");
        boolean flag = updateBatchById(entityList, entityList.size());
        if (!flag) {
            BusinessException.throwMessage("批量更新失败,请刷新重试");
        }
        return flag;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteBatchByDTO(Collection<ScheduleJobLog> entityList) {
        AssertUtil.isNotEmpty(entityList, "参数不能为空");
        boolean flag = deleteByIdsWithFill(entityList);
        if (!flag) {
            BusinessException.throwMessage("批量更新失败,请刷新重试");
        }
    }

    @Override
    public IPage<ScheduleJobLog> queryListByPage(ScheduleJobLogDTO dto) {
        AssertUtil.isNotNull(dto, "参数不能为空");
        IPage<ScheduleJobLog> page = new Page<>(dto.getCurrent(), dto.getSize());
        LambdaQueryWrapper<ScheduleJobLog> queryWrapper = new LambdaQueryWrapper<>();
        List<String> createTime = dto.getCreateTime();
        if (!CollectionUtils.isEmpty(createTime)) {
            String createTimeStart = createTime.get(0);
            String createTimeEnd = createTime.get(1);
            queryWrapper.between(ScheduleJobLog::getCreateTime, createTimeStart, createTimeEnd);
        }
        queryWrapper.orderByDesc(ScheduleJobLog::getCreateTime);
        queryWrapper.like(StringUtils.isNotBlank(dto.getJobName()),ScheduleJobLog::getJobName, dto.getJobName());
        queryWrapper.eq(dto.getJobId()!=null,ScheduleJobLog::getJobId, dto.getJobId());
        queryWrapper.eq(dto.getStatus()!=null,ScheduleJobLog::getStatus, dto.getStatus());
        return page(page, queryWrapper);
    }

}
