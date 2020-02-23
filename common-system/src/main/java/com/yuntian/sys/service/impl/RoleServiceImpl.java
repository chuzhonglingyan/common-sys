package com.yuntian.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
import java.util.function.Consumer;
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
        List<Role> roleList = getRoleByKey(dto.getKey());
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
    public void isEnable(Role dto) {
        AssertUtil.isNotNull(dto.getId(), "id不能为空");
        dto.setStatus(EnabledEnum.DISENABLED.getType());
        UpdateWrapper<Role> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("id", dto.getId());
        updateWrapper.set("status", EnabledEnum.ENABLED.getType());
        update(dto, updateWrapper);
    }

    @Override
    public void isDisEnable(Role dto) {
        AssertUtil.isNotNull(dto.getId(), "id不能为空");
        List<Long> operatorIdList = operatorRoleService.getOperatorIdListByRoleId(dto.getId());
        if (CollectionUtils.isNotEmpty(operatorIdList)) {
            BusinessException.throwMessage("角色下关联着用户,不能禁用");
        }

        dto.setStatus(EnabledEnum.ENABLED.getType());
        UpdateWrapper<Role> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("id", dto.getId());
        updateWrapper.set("status", EnabledEnum.DISENABLED.getType());
        update(dto, updateWrapper);
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
        PageVO<RoleVO> roleVoPage=new PageVO<RoleVO>(page(pageParam)){};
        List<RoleVO> roleVoList=roleVoPage.getRecords();
        if (CollectionUtils.isEmpty(roleVoList)){
            return roleVoPage;
        }
        List<Long> roleIdList=roleVoList.stream().map(RoleVO::getId).distinct().collect(Collectors.toList());
        List<RoleMenu> roleMenuList=roleMenuService.getRoleMenuList(roleIdList);
        Map<Long, List<Long>> groupByRoleId = roleMenuList.stream().collect(Collectors.groupingBy(RoleMenu::getRoleId,Collectors.mapping(RoleMenu::getMenuId,Collectors.toList())));
        roleVoPage.getRecords().forEach(roleVO -> {
            roleVO.setMenuIdList(groupByRoleId.getOrDefault(roleVO.getId(),new ArrayList<>()));
        });
        return roleVoPage;
    }

    @Override
    public void deleteBatchByDTO(Collection<Role> entityList) {

    }

    @Override
    public List<Role> getEnableList() {
        Map<String, Object> map = new HashMap<>();
        map.put("status", EnabledEnum.ENABLED.getType());
        return listByMap(map);
    }

    @Override
    public List<Role> getRoleByKey(String roleKey) {
        Map<String, Object> map = new HashMap<>();
        map.put("role", roleKey);
        return listByMap(map);
    }

}
