package com.yuntian.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuntian.architecture.data.BaseServiceImpl;
import com.yuntian.architecture.data.exception.BusinessException;
import com.yuntian.architecture.data.util.AssertUtil;
import com.yuntian.architecture.util.BeanCopyUtil;
import com.yuntian.sys.mapper.DictDetailMapper;
import com.yuntian.sys.model.dto.DictDetailSaveDTO;
import com.yuntian.sys.model.dto.DictDetailUpdateDTO;
import com.yuntian.sys.model.dto.DictQueyDetailDTO;
import com.yuntian.sys.model.entity.Dict;
import com.yuntian.sys.model.entity.DictDetail;
import com.yuntian.sys.service.DictDetailService;
import com.yuntian.sys.service.DictService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

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

    @Resource
    private DictService dictService;

    @Override
    public DictDetail getById(Serializable id) {
        return super.getById(id);
    }


    @Override
    public void saveByDTO(DictDetailSaveDTO dto) {
        AssertUtil.isNotNull(dto, "参数不能为空");
        DictDetail dictDetail = BeanCopyUtil.copyProperties(dto, DictDetail.class);
        super.save(dictDetail);
    }


    @Override
    public void updateByDTO(DictDetailUpdateDTO dto) {
        AssertUtil.isNotNull(dto, "参数不能为空");
        DictDetail dictDetail = BeanCopyUtil.copyProperties(dto, DictDetail.class);
        boolean flag = updateById(dictDetail);
        AssertUtil.isNotTrue(flag, "更新失败");
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

    @Override
    public void deleteByDictId(Long dictId) {
        LambdaQueryWrapper<DictDetail> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DictDetail::getDictId, dictId);
        boolean flag =  remove(lambdaQueryWrapper);
        AssertUtil.isNotTrue(flag, "删除失败");
    }


    @Override
    public void deleteByDictIdList(List<Long> dictIdList) {
        LambdaQueryWrapper<DictDetail> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(DictDetail::getDictId, dictIdList);
        boolean flag =  remove(lambdaQueryWrapper);
        AssertUtil.isNotTrue(flag, "删除失败");
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
    public void deleteBatchByDTO(Long operatorId, List<Long> idList) {
        AssertUtil.isNotEmpty(idList, "集合不能为空");
        Collection<DictDetail> entityList = new ArrayList<>();
        idList.forEach(id -> {
            DictDetail dict = new DictDetail();
            dict.setId(id);
            dict.setUpdateId(operatorId);
            entityList.add(dict);
        });
        boolean flag = deleteByIdsWithFill(entityList);
        AssertUtil.isNotTrue(flag, "删除失败,请刷新重试");
    }


    @Override
    public IPage<DictDetail> queryListByPage(DictQueyDetailDTO dto) {
        AssertUtil.isNotNull(dto, "参数不能为空");
        AssertUtil.isNotBlank(dto.getDictName(), "所属字典不能为空");
        Dict dict = dictService.getDictByName(dto.getDictName());
        IPage<DictDetail> page = new Page<>(dto.getCurrent(), dto.getSize());
        LambdaQueryWrapper<DictDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DictDetail::getDictId, dict.getId());
        queryWrapper.eq(StringUtils.isNotBlank(dto.getLabel()), DictDetail::getLabel, dto.getLabel());
        queryWrapper.orderByAsc(DictDetail::getSort);
        queryWrapper.orderByDesc(DictDetail::getUpdateTime);
        return page(page, queryWrapper);
    }

}
