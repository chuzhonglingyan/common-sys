package com.yuntian.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuntian.architecture.data.BaseServiceImpl;
import com.yuntian.architecture.data.exception.BusinessException;
import com.yuntian.architecture.data.util.AssertUtil;
import com.yuntian.architecture.util.BeanCopyUtil;
import com.yuntian.sys.common.dict.EnabledEnum;
import com.yuntian.sys.mapper.RoleMapper;
import com.yuntian.sys.model.dto.RoleQueryDTO;
import com.yuntian.sys.model.dto.RoleSaveDTO;
import com.yuntian.sys.model.dto.RoleUpdateDTO;
import com.yuntian.sys.model.entity.Role;
import com.yuntian.sys.model.entity.RoleMenu;
import com.yuntian.sys.model.vo.PageVO;
import com.yuntian.sys.model.vo.RoleVO;
import com.yuntian.sys.service.OperatorRoleService;
import com.yuntian.sys.service.RoleMenuService;
import com.yuntian.sys.service.RoleService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
 * 后台系统-角色表 服务实现类
 * </p>
 *
 * @author yuntian
 * @since 2020-01-31
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    private OperatorRoleService operatorRoleService;

    @Resource
    private RoleMenuService roleMenuService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveByDTO(RoleSaveDTO dto) {
        Role role = BeanCopyUtil.copyProperties(dto, Role.class);
        boolean flag = save(role);
        AssertUtil.isNotTrue(flag, "保存失败");
        List<Role> roleList = getRoleByKey(dto.getRoleKey());
        if (CollectionUtils.isNotEmpty(roleList) && roleList.size() > 1) {
            BusinessException.throwMessage("该角色已经存在");
        }
    }

    @Override
    public void updateByDTO(RoleUpdateDTO dto) {
        getById(dto.getId());
        Role role = BeanCopyUtil.copyProperties(dto, Role.class);
        boolean flag = updateById(role);
        AssertUtil.isNotTrue(flag, "更新失败");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteByDTO(Role dto) {
        AssertUtil.isNotNull(dto.getId(), "id不能为空");
        List<Long> operatorIdList = operatorRoleService.getOperatorIdListByRoleId(dto.getId());
        if (CollectionUtils.isNotEmpty(operatorIdList)) {
            BusinessException.throwMessage("角色下关联着用户,不能删除");
        }
        //删除关系
        roleMenuService.deleteByRoleId(dto.getId());
        boolean flag = deleteByIdWithFill(dto);
        AssertUtil.isNotTrue(flag, "删除失败,请刷新重试");
    }

    @Override
    public void enable(Role dto) {
        AssertUtil.isNotNull(dto.getId(), "id不能为空");
        dto.setStatus(EnabledEnum.ENABLED.getValue());
        LambdaQueryWrapper<Role> updateWrapper = new LambdaQueryWrapper<>();
        updateWrapper.eq(Role::getId, dto.getId());
        updateWrapper.eq(Role::getStatus, EnabledEnum.DISENABLED.getValue());
        boolean flag = update(dto, updateWrapper);
        AssertUtil.isNotTrue(flag, "启用失败,请刷新页面");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void disEnable(Role dto) {
        AssertUtil.isNotNull(dto.getId(), "id不能为空");
        dto.setStatus(EnabledEnum.DISENABLED.getValue());
        LambdaQueryWrapper<Role> updateWrapper = new LambdaQueryWrapper<>();
        updateWrapper.eq(Role::getId, dto.getId());
        updateWrapper.eq(Role::getStatus, EnabledEnum.ENABLED.getValue());
        boolean flag = update(dto, updateWrapper);
        AssertUtil.isNotTrue(flag, "禁用失败,请刷新页面");
        List<Long> operatorIdList = operatorRoleService.getOperatorIdListByRoleId(dto.getId());
        if (CollectionUtils.isNotEmpty(operatorIdList)) {
            BusinessException.throwMessage("角色下关联着用户,不能禁用");
        }
    }


    @Override
    public Role getById(Serializable id) {
        AssertUtil.isNotNull(id, "参数不能为空");
        Role role = super.getById(id);
        AssertUtil.isNotNull(role, "该角色不存在");
        return role;
    }

    @Override
    public PageVO<RoleVO> queryListByPage(RoleQueryDTO dto) {
        AssertUtil.isNotNull(dto, "参数不能为空");
        IPage<Role> pageParam = new Page<>(dto.getCurrent(), dto.getSize());
        PageVO<RoleVO> roleVoPage = new PageVO<RoleVO>(page(pageParam)) {
        };
        List<RoleVO> roleVoList = roleVoPage.getRecords();
        if (CollectionUtils.isEmpty(roleVoList)) {
            return roleVoPage;
        }
        List<Long> roleIdList = roleVoList.stream().map(RoleVO::getId).distinct().collect(Collectors.toList());
        List<RoleMenu> roleMenuList = roleMenuService.getRoleMenuList(roleIdList);
        Map<Long, List<Long>> groupByRoleId = roleMenuList.stream().collect(Collectors.groupingBy(RoleMenu::getRoleId, Collectors.mapping(RoleMenu::getMenuId, Collectors.toList())));
        roleVoPage.getRecords().forEach(roleVO -> {
            roleVO.setMenuIdList(groupByRoleId.getOrDefault(roleVO.getId(), new ArrayList<>()));
        });
        return roleVoPage;
    }


    @Override
    public List<Role> getEnableList() {
        LambdaQueryWrapper<Role> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Role::getStatus, EnabledEnum.ENABLED.getValue());
        return list(lambdaQueryWrapper);
    }

    @Override
    public List<Role> getRoleByKey(String roleKey) {
        LambdaQueryWrapper<Role> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Role::getRoleKey, roleKey);
        return list(lambdaQueryWrapper);
    }

    @Override
    public RoleVO getInfo(Long id) {
        Role role = getById(id);
        RoleVO roleVO = BeanCopyUtil.copyProperties(role, RoleVO.class);
        List<Long> menuIdList = roleMenuService.getMenuIdListByRoleId(id);
        roleVO.setMenuIdList(menuIdList);
        return roleVO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void changeState(Role dto) {
        AssertUtil.isNotNull(dto.getId(), "id不能为空");
        AssertUtil.isNotNull(dto.getStatus(), "状态不能为空");
        if (dto.getStatus() == 0) {
            enable(dto);
        } else {
            disEnable(dto);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteBatchByDTO(Long operatorId, List<Long> idList) {
        AssertUtil.isNotEmpty(idList, "集合不能为空");
        Collection<Role> entityList = new ArrayList<>();
        idList.forEach(id -> {
            Role roleTemp = getById(id);
            if (roleTemp.getStatus() == EnabledEnum.ENABLED.getValue()) {
                BusinessException.throwMessage("角色处于启用状态，无法删除.");
            }
            List<Long> operatorIdList = operatorRoleService.getOperatorIdListByRoleId(id);
            if (CollectionUtils.isNotEmpty(operatorIdList)) {
                BusinessException.throwMessage("角色下关联着用户,不能删除");
            }
            //删除关系
            roleMenuService.deleteByRoleId(id);
            Role role = new Role();
            role.setId(id);
            role.setUpdateId(operatorId);
            entityList.add(role);
        });
        boolean flag = deleteByIdsWithFill(entityList);
        AssertUtil.isNotTrue(flag, "删除失败,请刷新重试");
    }


}
