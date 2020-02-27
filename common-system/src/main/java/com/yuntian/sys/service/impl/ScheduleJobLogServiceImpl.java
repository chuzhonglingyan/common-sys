package com.yuntian.sys.service.impl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuntian.sys.model.dto.ScheduleJobLogDTO;
import com.yuntian.sys.model.entity.ScheduleJobLog;
import com.yuntian.sys.mapper.ScheduleJobLogMapper;
import com.yuntian.sys.service.ScheduleJobLogService;
import com.yuntian.architecture.data.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.yuntian.architecture.data.util.AssertUtil;
import com.yuntian.architecture.data.exception.BusinessException;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collection;
import java.util.Objects;
import java.io.Serializable;

/**
 * <p>
 * 系统-定时任务日志 服务实现类
 * </p>
 *
 * @author yuntian
 * @since 2020-02-27
 */
@Service
public class ScheduleJobLogServiceImpl extends BaseServiceImpl<ScheduleJobLogMapper, ScheduleJobLog> implements ScheduleJobLogService {

    @Override
    public IPage<ScheduleJobLog> queryListByPage(ScheduleJobLogDTO dto) {
        AssertUtil.isNotNull(dto, "参数不能为空");
        IPage<ScheduleJobLog> page=new Page<>(dto.getCurrent(),dto.getSize());
        return page(page);
    }


    @Override
    public ScheduleJobLog getById(Serializable id) {
        return super.getById(id);
    }


    @Override
    public boolean save(ScheduleJobLog dto) {
        AssertUtil.isNotNull(dto, "参数不能为空");
        return super.save(dto);
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
}
