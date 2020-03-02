package com.yuntian.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuntian.architecture.data.BaseServiceImpl;
import com.yuntian.architecture.data.exception.BusinessException;
import com.yuntian.architecture.data.util.AssertUtil;
import com.yuntian.architecture.util.BeanCopyUtil;
import com.yuntian.sys.mapper.DictMapper;
import com.yuntian.sys.model.dto.DictQueryDTO;
import com.yuntian.sys.model.entity.Dict;
import com.yuntian.sys.model.entity.DictDetail;
import com.yuntian.sys.model.vo.DictVO;
import com.yuntian.sys.service.DictDetailService;
import com.yuntian.sys.service.DictService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

/**
 * <p>
 * 后台系统-数据字典 服务实现类
 * </p>
 *
 * @author yuntian
 * @since 2020-02-27
 */
@CacheConfig(cacheNames = "dict")
@Service
public class DictServiceImpl extends BaseServiceImpl<DictMapper, Dict> implements DictService {

    @Resource
    private DictDetailService dictDetailService;

    @Override
    public Dict getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    public void saveByDTO(Dict dto) {
        AssertUtil.isNotNull(dto, "参数不能为空");
        AssertUtil.isNotBlank(dto.getName(), "字典名称不能为空");
        try {
            super.save(dto);
        } catch (DuplicateKeyException e) {
            BusinessException.throwMessage("字典名称已存在");
        }
    }


    @Override
    public void updateByDTO(Dict dto) {
        AssertUtil.isNotNull(dto, "参数不能为空");
        AssertUtil.isNotNull(dto.getId(), "id不能为空");
        boolean flag = super.updateById(dto);
        if (!flag) {
            BusinessException.throwMessage("更新失败,请刷新重试");
        }
    }


    @Override
    public void deleteByDTO(Dict dto) {
        AssertUtil.isNotNull(dto, "参数不能为空");
        AssertUtil.isNotNull(dto.getId(), "id不能为空");
        Dict dict = getById(dto.getId());
        boolean flag = deleteByIdWithFill(dto);
        if (!flag) {
            BusinessException.throwMessage("删除失败,请刷新重试");
        }
        dictDetailService.deleteByDictId(dict.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteBatchByDTO(Long operatorId, List<Long> idList) {
        AssertUtil.isNotEmpty(idList, "集合不能为空");
        Collection<Dict> entityList = new ArrayList<>();
        dictDetailService.deleteByDictIdList(idList);
        idList.forEach(id -> {
            Dict dict = new Dict();
            dict.setId(id);
            dict.setUpdateId(operatorId);
            entityList.add(dict);
        });
        boolean flag = deleteByIdsWithFill(entityList);
        AssertUtil.isNotTrue(flag, "删除失败,请刷新重试");
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateBatchById(Collection<Dict> entityList) {
        AssertUtil.isNotEmpty(entityList, "参数不能为空");
        boolean flag = updateBatchById(entityList, entityList.size());
        if (!flag) {
            BusinessException.throwMessage("批量更新失败,请刷新重试");
        }
        return flag;
    }


    @Override
    public IPage<Dict> queryListByPage(DictQueryDTO dto) {
        AssertUtil.isNotNull(dto, "参数不能为空");
        IPage<Dict> page = new Page<>(dto.getCurrent(), dto.getSize());
        LambdaQueryWrapper<Dict> queryWrapper = new LambdaQueryWrapper<>();
        String blurry = dto.getBlurry();
        queryWrapper.or(StringUtils.isNotBlank(blurry), dictLambdaQueryWrapper -> {
            dictLambdaQueryWrapper.like(Dict::getName, blurry);
        }).or(StringUtils.isNotBlank(blurry), dictLambdaQueryWrapper -> {
            dictLambdaQueryWrapper.like(Dict::getRemark, blurry);
        });
        queryWrapper.orderByDesc(Dict::getUpdateTime);
        return page(page, queryWrapper);
    }

    @Cacheable
    @Override
    public List<DictVO> getAll() {
        List<Dict> list = list();
        if (CollectionUtils.isEmpty(list)) {
            new ArrayList<>();
        }
        List<DictDetail> listDetail = dictDetailService.list();
        Map<Long, List<DictDetail>> dictDetailMap = listDetail.stream().collect(Collectors.groupingBy(DictDetail::getDictId));
        List<DictVO> voList = new ArrayList<>(list.size());
        list.forEach(dict -> {
            DictVO dictVO = BeanCopyUtil.copyProperties(dict, DictVO.class);
            dictVO.setDictDetails(dictDetailMap.getOrDefault(dict.getId(), new ArrayList<>()));
            voList.add(dictVO);
        });
        return voList;
    }

    @Override
    public Dict getDictByName(String name) {
        AssertUtil.isNotBlank(name, "参数不能为空");
        LambdaQueryWrapper<Dict> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Objects.nonNull(name), Dict::getName, name);
        return getOne(queryWrapper);
    }

}
