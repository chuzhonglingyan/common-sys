package com.yuntian.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuntian.architecture.data.BaseServiceImpl;
import com.yuntian.architecture.data.exception.BusinessException;
import com.yuntian.architecture.data.util.AssertUtil;
import com.yuntian.architecture.util.BeanCopyUtil;
import com.yuntian.sys.common.dict.EnabledEnum;
import com.yuntian.sys.mapper.OperatorRoleMapper;
import com.yuntian.sys.model.dto.OperatorRoleDTO;
import com.yuntian.sys.model.entity.OperatorRole;
import com.yuntian.sys.model.entity.Role;
import com.yuntian.sys.model.vo.RoleVO;
import com.yuntian.sys.service.OperatorRoleService;
import com.yuntian.sys.service.RoleService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

/**
 * <p>
 * 后台系统-用户角色关系表 服务实现类
 * </p>
 *
 * @author yuntian
 * @since 2020-01-31
 */
@Service
public class OperatorRoleServiceImpl extends BaseServiceImpl<OperatorRoleMapper, OperatorRole> implements OperatorRoleService {

    @Resource
    private RoleService roleService;

    @Override
    public IPage<OperatorRole> queryListByPage(OperatorRoleDTO dto) {
        AssertUtil.isNotNull(dto, "参数不能为空");
        IPage<OperatorRole> page = new Page<>(dto.getCurrent(), dto.getSize());
        return page(page);
    }


    @Override
    public OperatorRole getById(Serializable id) {
        return super.getById(id);
    }


    @Override
    public boolean save(OperatorRole dto) {
        AssertUtil.isNotNull(dto, "参数不能为空");
        return super.save(dto);
    }


    @Override
    public void updateByDTO(OperatorRole dto) {
        AssertUtil.isNotNull(dto, "参数不能为空");
        AssertUtil.isNotNull(dto.getId(), "id不能为空");
        boolean flag = super.updateById(dto);
        if (!flag) {
            BusinessException.throwMessage("更新失败,请刷新重试");
        }
    }


    @Override
    public void deleteByDTO(OperatorRole dto) {
        AssertUtil.isNotNull(dto, "参数不能为空");
        AssertUtil.isNotNull(dto.getId(), "id不能为空");
        boolean flag = deleteByIdWithFill(dto);
        if (!flag) {
            BusinessException.throwMessage("删除失败,请刷新重试");
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateBatchById(Collection<OperatorRole> entityList) {
        AssertUtil.isNotEmpty(entityList, "参数不能为空");
        boolean flag = updateBatchById(entityList, entityList.size());
        if (!flag) {
            BusinessException.throwMessage("批量更新失败,请刷新重试");
        }
        return flag;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteBatchByDTO(Collection<OperatorRole> entityList) {
        AssertUtil.isNotEmpty(entityList, "参数不能为空");
        boolean flag = deleteByIdsWithFill(entityList);
        if (!flag) {
            BusinessException.throwMessage("批量更新失败,请刷新重试");
        }
    }

    @Override
    public void saveRoleListByOperatorId(OperatorRoleDTO dto) {
        AssertUtil.isNotNull(dto.getOperaterId(), "用户id不能为空");
        saveRoleListByOperatorId(dto.getOperaterId(),dto.getRoleList());
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveRoleListByOperatorId(Long operatorId,List<Long> roleIdList) {
        if (CollectionUtils.isEmpty(roleIdList)) {
            BusinessException.throwMessage("请选择角色");
        }
        //删除原先的关联关系
        deleteByOperatorId(operatorId);
        List<OperatorRole> operatorRoleList = new ArrayList<>();
        for (Long roleId : roleIdList) {
            OperatorRole operatorRole = new OperatorRole();
            operatorRole.setUserId(operatorId);
            operatorRole.setRoleId(roleId);
            operatorRole.setCreateId(operatorId);
            operatorRole.setUpdateId(operatorId);
            operatorRoleList.add(operatorRole);
        }
        saveBatch(operatorRoleList);
    }

    @Override
    public void deleteByOperatorId(Long operatorId) {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", operatorId);
        removeByMap(map);
    }

    @Override
    public List<Long> getRoleIdListByOperatorId(Long operatorId) {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", operatorId);
        List<OperatorRole> list = super.listByMap(map);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        return list.stream().map(OperatorRole::getRoleId).collect(Collectors.toList());
    }

    @Override
    public Long getRoleIdByOperatorId(Long operatorId) {
        LambdaQueryWrapper<OperatorRole> lambdaQueryWrapper = new QueryWrapper<OperatorRole>().lambda()
                .eq(OperatorRole::getUserId, operatorId)
                .orderByDesc(OperatorRole::getCreateTime);
        List<OperatorRole> operatorRoleList = list(lambdaQueryWrapper);
        if (CollectionUtils.isEmpty(operatorRoleList)) {
            return null;
        }
        List<Long> roleId = operatorRoleList.stream().map(OperatorRole::getRoleId).collect(Collectors.toList());
        return roleId.get(0);
    }

    @Override
    public List<Long> getOperatorIdListByRoleId(Long roleId) {
        Map<String, Object> map = new HashMap<>();
        map.put("role_id", roleId);
        List<OperatorRole> list = super.listByMap(map);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        return list.stream().map(OperatorRole::getUserId).collect(Collectors.toList());
    }

    @Override
    public Role getEnableRoleByOperatorId(Long operatorId) {
        Long roleId = getRoleIdByOperatorId(operatorId);
        return roleService.getById(roleId);
    }

    @Override
    public List<Role> getEnableListByOperatorId(Long operatorId) {
        List<Long> roleIdList = getRoleIdListByOperatorId(operatorId);
        if (CollectionUtils.isEmpty(roleIdList)){
            return new ArrayList<>();
        }
        LambdaQueryWrapper<Role> lambdaQueryWrapper = new QueryWrapper<Role>().lambda()
                .eq(Role::getStatus, EnabledEnum.ENABLED.getValue())
                .in(Role::getId, roleIdList);
        return roleService.list(lambdaQueryWrapper);
    }

    /**
     * 提供给用户选择角色
     *
     * @param operatorId
     * @return
     */
    @Override
    public List<RoleVO> getRoleListByOperatorId(Long operatorId) {
        //获取用户拥有的角色
        List<Long> roleIdList = getRoleIdListByOperatorId(operatorId);
        //获取所有可用的角色
        List<Role> roleList = roleService.getEnableList();
        if (com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isEmpty(roleList)) {
            return new ArrayList<>();
        }
        Map<Long, Role> roleMap = roleList.stream().collect(Collectors.toMap(Role::getId, a -> a, (k1, k2) -> k1));
        List<RoleVO> roleVoList = new ArrayList<>();
        for (Long roleId : roleIdList) {
            if (roleMap.containsKey(roleId)) {
                Role role = roleMap.get(roleId);
                RoleVO roleVO = new RoleVO();
                BeanCopyUtil.copyProperties(role, roleVO);
                roleVO.setIsChecked(true);
                roleVoList.add(roleVO);
            }
        }
        return roleVoList;
    }


}
