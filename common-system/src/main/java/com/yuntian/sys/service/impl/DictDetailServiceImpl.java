package com.yuntian.sys.service.impl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuntian.sys.model.dto.DictDetailDTO;
import com.yuntian.sys.model.entity.DictDetail;
import com.yuntian.sys.mapper.DictDetailMapper;
import com.yuntian.sys.service.DictDetailService;
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
 * 后台系统-数据字典详情 服务实现类
 * </p>
 *
 * @author yuntian
 * @since 2020-02-27
 */
@Service
public class DictDetailServiceImpl extends BaseServiceImpl<DictDetailMapper, DictDetail> implements DictDetailService {

    @Override
    public IPage<DictDetail> queryListByPage(DictDetailDTO dto) {
        AssertUtil.isNotNull(dto, "参数不能为空");
        IPage<DictDetail> page=new Page<>(dto.getCurrent(),dto.getSize());
        return page(page);
    }


    @Override
    public DictDetail getById(Serializable id) {
        return super.getById(id);
    }


    @Override
    public boolean save(DictDetail dto) {
        AssertUtil.isNotNull(dto, "参数不能为空");
        return super.save(dto);
    }


    @Override
    public void updateByDTO(DictDetail dto) {
        AssertUtil.isNotNull(dto, "参数不能为空");
        AssertUtil.isNotNull(dto.getId(), "id不能为空");
        boolean flag = super.updateById(dto);
        if (!flag) {
            BusinessException.throwMessage("更新失败,请刷新重试");
        }
    }


    @Override
    public void deleteByDTO(DictDetail dto) {
        AssertUtil.isNotNull(dto, "参数不能为空");
        AssertUtil.isNotNull(dto.getId(), "id不能为空");
        boolean flag = deleteByIdWithFill(dto);
        if (!flag) {
            BusinessException.throwMessage("删除失败,请刷新重试");
        }
    }



    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateBatchById(Collection<DictDetail> entityList) {
        AssertUtil.isNotEmpty(entityList, "参数不能为空");
        boolean flag = updateBatchById(entityList, entityList.size());
        if (!flag) {
            BusinessException.throwMessage("批量更新失败,请刷新重试");
        }
        return flag;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteBatchByDTO(Collection<DictDetail> entityList) {
        AssertUtil.isNotEmpty(entityList, "参数不能为空");
        boolean flag = deleteByIdsWithFill(entityList);
        if (!flag) {
            BusinessException.throwMessage("批量更新失败,请刷新重试");
        }
    }
}
