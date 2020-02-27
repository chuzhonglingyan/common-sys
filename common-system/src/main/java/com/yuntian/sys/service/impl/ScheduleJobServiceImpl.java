package com.yuntian.sys.service.impl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuntian.sys.model.dto.ScheduleJobDTO;
import com.yuntian.sys.model.entity.ScheduleJob;
import com.yuntian.sys.mapper.ScheduleJobMapper;
import com.yuntian.sys.service.ScheduleJobService;
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
 * 系统-定时任务 服务实现类
 * </p>
 *
 * @author yuntian
 * @since 2020-02-27
 */
@Service
public class ScheduleJobServiceImpl extends BaseServiceImpl<ScheduleJobMapper, ScheduleJob> implements ScheduleJobService {

    @Override
    public IPage<ScheduleJob> queryListByPage(ScheduleJobDTO dto) {
        AssertUtil.isNotNull(dto, "参数不能为空");
        IPage<ScheduleJob> page=new Page<>(dto.getCurrent(),dto.getSize());
        return page(page);
    }


    @Override
    public ScheduleJob getById(Serializable id) {
        return super.getById(id);
    }


    @Override
    public boolean save(ScheduleJob dto) {
        AssertUtil.isNotNull(dto, "参数不能为空");
        return super.save(dto);
    }


    @Override
    public void updateByDTO(ScheduleJob dto) {
        AssertUtil.isNotNull(dto, "参数不能为空");
        AssertUtil.isNotNull(dto.getId(), "id不能为空");
        boolean flag = super.updateById(dto);
        if (!flag) {
            BusinessException.throwMessage("更新失败,请刷新重试");
        }
    }


    @Override
    public void deleteByDTO(ScheduleJob dto) {
        AssertUtil.isNotNull(dto, "参数不能为空");
        AssertUtil.isNotNull(dto.getId(), "id不能为空");
        boolean flag = deleteByIdWithFill(dto);
        if (!flag) {
            BusinessException.throwMessage("删除失败,请刷新重试");
        }
    }



    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateBatchById(Collection<ScheduleJob> entityList) {
        AssertUtil.isNotEmpty(entityList, "参数不能为空");
        boolean flag = updateBatchById(entityList, entityList.size());
        if (!flag) {
            BusinessException.throwMessage("批量更新失败,请刷新重试");
        }
        return flag;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteBatchByDTO(Collection<ScheduleJob> entityList) {
        AssertUtil.isNotEmpty(entityList, "参数不能为空");
        boolean flag = deleteByIdsWithFill(entityList);
        if (!flag) {
            BusinessException.throwMessage("批量更新失败,请刷新重试");
        }
    }
}
