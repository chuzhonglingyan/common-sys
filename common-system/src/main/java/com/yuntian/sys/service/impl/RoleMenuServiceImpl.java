package com.yuntian.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuntian.architecture.data.BaseServiceImpl;
import com.yuntian.architecture.data.exception.BusinessException;
import com.yuntian.architecture.data.util.AssertUtil;
import com.yuntian.architecture.util.BeanCopyUtil;
import com.yuntian.sys.mapper.RoleMenuMapper;

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
import com.yuntian.sys.model.dto.RoleMenuDTO;
import com.yuntian.sys.model.entity.Menu;
import com.yuntian.sys.model.entity.RoleMenu;
import com.yuntian.sys.model.vo.MenuVO;
import com.yuntian.sys.service.MenuService;
import com.yuntian.sys.service.RoleMenuService;

/**
 * <p>
 * 后台系统-角色菜单关系表 服务实现类
 * </p>
 *
 * @author yuntian
 * @since 2020-01-31
 */
@Service
public class RoleMenuServiceImpl extends BaseServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {


    @Resource
    private MenuService menuService;

    /**
     * 查询角色权限列表
     *
     * @param roleId
     * @return
     */
    @Override
    public List<MenuVO> getMenuListByRoleId(Long roleId) {
        List<Menu> menuEnableList = menuService.getEnableMenuList();
        List<Long> menuIdList = getMenuIdListByRoleId(roleId);
        List<MenuVO> menuSeeVoList = new ArrayList<>();
        for (Menu tempMenu : menuEnableList) {
            if (menuIdList.contains(tempMenu.getId())) {
                MenuVO menuVO = new MenuVO();
                BeanCopyUtil.copyProperties(tempMenu, menuVO);
                menuVO.setIsChecked(true);
                menuSeeVoList.add(menuVO);
            }
        }
        return menuSeeVoList;
    }

    /**
     * 查询角色拥有的权限
     *
     * @param roleId
     * @return
     */
    @Override
    public List<Menu> getEnableMenuListByRoleId(Long roleId) {
        List<Long> menuIdList = getMenuIdListByRoleId(roleId);
        if (CollectionUtils.isEmpty(menuIdList)) {
            return new ArrayList<>();
        }
        return menuService.getEnableMenuList(menuIdList);
    }

    /**
     * @return
     */
    @Override
    public List<Menu> getEnableMenuListByRoleIds(List<Long> roleIdList) {
        List<Long> menuIdList = getMenuIdListByRoleId(roleIdList);
        if (CollectionUtils.isEmpty(menuIdList)) {
            return new ArrayList<>();
        }
        return menuService.getEnableMenuList(menuIdList);
    }

    /**
     * 保存角色选择的菜单
     *
     * @param roleMenuDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMenuListByRoleId(RoleMenuDTO roleMenuDTO) {
        AssertUtil.isNotNull(roleMenuDTO, "参数不能为空");
        AssertUtil.isNotNull(roleMenuDTO.getRoleId(), "角色id不能为空");
        AssertUtil.isNotNull(roleMenuDTO.getMenuIdList(), "选择菜单不能为空");
        //删除原先的关联关系
        deleteByRoleId(roleMenuDTO.getRoleId());
        List<Long> list = roleMenuDTO.getMenuIdList();
        List<RoleMenu> roleMenuList = new ArrayList<>();
        for (Long menuId : list) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleMenuDTO.getRoleId());
            roleMenu.setMenuId(menuId);
            roleMenu.setIsChecked(1);
            roleMenu.setCreateId(roleMenuDTO.getCreateId());
            roleMenu.setUpdateId(roleMenuDTO.getUpdateId());
            roleMenuList.add(roleMenu);
        }
        saveBatch(roleMenuList);
    }

    @Override
    public IPage<RoleMenu> queryListByPage(RoleMenuDTO dto) {
        AssertUtil.isNotNull(dto, "参数不能为空");
        IPage<RoleMenu> page = new Page<>(dto.getCurrent(), dto.getSize());
        return page(page);
    }


    @Override
    public RoleMenu getById(Serializable id) {
        return super.getById(id);
    }


    @Override
    public boolean save(RoleMenu dto) {
        AssertUtil.isNotNull(dto, "参数不能为空");
        return super.save(dto);
    }


    @Override
    public void updateByDTO(RoleMenu dto) {
        AssertUtil.isNotNull(dto, "参数不能为空");
        AssertUtil.isNotNull(dto.getId(), "id不能为空");
        boolean flag = super.updateById(dto);
        if (!flag) {
            BusinessException.throwMessage("更新失败,请刷新重试");
        }
    }


    @Override
    public void deleteByDTO(RoleMenu dto) {
        AssertUtil.isNotNull(dto, "参数不能为空");
        AssertUtil.isNotNull(dto.getId(), "id不能为空");
        boolean flag = deleteByIdWithFill(dto);
        if (!flag) {
            BusinessException.throwMessage("删除失败,请刷新重试");
        }
    }

    @Override
    public void deleteByRoleId(Long roleId) {
        Map<String, Object> map = new HashMap<>();
        map.put("role_id", roleId);
        removeByMap(map);
    }

    @Override
    public void deleteByMenuId(Long menuId) {
        Map<String, Object> map = new HashMap<>();
        map.put("menu_id", menuId);
        removeByMap(map);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteBatchByDTO(Collection<RoleMenu> entityList) {
        AssertUtil.isNotEmpty(entityList, "参数不能为空");
        boolean flag = deleteByIdsWithFill(entityList);
        if (!flag) {
            BusinessException.throwMessage("批量更新失败,请刷新重试");
        }
    }

    @Override
    public List<Long> getMenuIdListByRoleId(Long roleId) {
        Map<String, Object> map = new HashMap<>();
        map.put("role_id", roleId);
        List<RoleMenu> list = super.listByMap(map);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        return list.stream().map(RoleMenu::getMenuId).collect(Collectors.toList());
    }

    @Override
    public List<Long> getMenuIdListByRoleId(List<Long> roleIdList) {
        LambdaQueryWrapper<RoleMenu> lambdaQueryWrapper = new QueryWrapper<RoleMenu>().lambda()
                .in(RoleMenu::getRoleId, roleIdList);
        List<RoleMenu> list = list(lambdaQueryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        return list.stream().map(RoleMenu::getMenuId).distinct().collect(Collectors.toList());
    }


}
