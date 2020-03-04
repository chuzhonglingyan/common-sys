package com.yuntian.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuntian.architecture.data.BaseServiceImpl;
import com.yuntian.architecture.data.exception.BusinessException;
import com.yuntian.architecture.data.util.AssertUtil;
import com.yuntian.architecture.data.util.TreeUtil;
import com.yuntian.architecture.util.BeanCopyUtil;
import com.yuntian.constant.SysConstants;
import com.yuntian.sys.enums.EnabledEnum;
import com.yuntian.sys.enums.MenuTypeEnum;
import com.yuntian.sys.mapper.MenuMapper;
import com.yuntian.sys.model.dto.MenuQueryDTO;
import com.yuntian.sys.model.dto.MenuSaveDTO;
import com.yuntian.sys.model.dto.MenuUpdateDTO;
import com.yuntian.sys.model.entity.Menu;
import com.yuntian.sys.model.entity.Role;
import com.yuntian.sys.model.vo.MenuComponentVo;
import com.yuntian.sys.model.vo.MenuTreeLabelVO;
import com.yuntian.sys.model.vo.MenuTreeVO;
import com.yuntian.sys.model.vo.PageVO;
import com.yuntian.sys.service.MenuService;
import com.yuntian.sys.service.OperatorRoleService;
import com.yuntian.sys.service.RoleMenuService;
import com.yuntian.util.ComponentUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;


/**
 * <p>
 * 后台系统-菜单表 服务实现类
 * </p>
 *
 * @author yuntian
 * @since 2020-01-31
 */
@Service
public class MenuServiceImpl extends BaseServiceImpl<MenuMapper, Menu> implements MenuService {

    @Resource
    private RoleMenuService roleMenuService;

    @Resource
    private OperatorRoleService operatorRoleService;


    /**
     * 保存菜单
     *
     * @param dto
     */
    @Override
    public void saveByDTO(MenuSaveDTO dto) {
        if (dto.getPid() == 0) {
            dto.setLevel(SysConstants.ONE);
            dto.setType(MenuTypeEnum.ROOT.getValue());
        } else {
            Menu parentMenu = getParentMenu(dto.getPid());
            if (parentMenu.getType() == MenuTypeEnum.BUTTON.getValue()) {
                BusinessException.throwMessage("操作类型菜单不能添加子级菜单");
            }
            dto.setLevel(parentMenu.getLevel() + 1);
            if (dto.getSort() == null) {
                List<Menu> brotherList = findDirectChildByPid(dto.getPid());
                Integer sort = brotherList == null ? 1 : brotherList.size() + 1;
                dto.setSort(sort);
            }
        }
        if (dto.getType() <= 1 && StringUtils.isBlank(dto.getPath())) {
            BusinessException.throwMessage("菜单路径不能为空");
        } else {
            dto.setPath("");
        }
        Menu menu = BeanCopyUtil.copyProperties(dto, Menu.class);
        boolean flag = save(menu);
        AssertUtil.isNotTrue(flag, "保存失败");
    }


    @Override
    public void updateByDTO(MenuUpdateDTO dto) {
        getById(dto.getId());
        Menu menu = BeanCopyUtil.copyProperties(dto, Menu.class);
        boolean flag = updateById(menu);
        AssertUtil.isNotTrue(flag, "更新失败");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteByDTO(Menu dto) {
        AssertUtil.isNotNull(dto.getId(), "id不能为空");
        Menu menu = getById(dto.getId());
        if (menu.getStatus() == EnabledEnum.DISENABLED.getValue()) {
            BusinessException.throwMessage("菜单处于冻结状态，无法删除.");
        }
        List<Menu> list = findDirectChildByPid(dto.getId());
        AssertUtil.isEmpty(list, "存在子菜单，不能删除");
        boolean flag = deleteByIdWithFill(dto);
        AssertUtil.isNotTrue(flag, "删除失败,请刷新重试");
        roleMenuService.deleteByMenuId(dto.getId());
    }


    @Override
    public void enable(Menu dto) {
        AssertUtil.isNotNull(dto.getId(), "id不能为空");
        dto.setStatus(EnabledEnum.ENABLED.getValue());
        LambdaQueryWrapper<Menu> updateWrapper = new LambdaQueryWrapper<>();
        updateWrapper.eq(Menu::getId, dto.getId());
        updateWrapper.eq(Menu::getStatus, EnabledEnum.DISENABLED.getValue());
        boolean flag = update(dto, updateWrapper);
        AssertUtil.isNotTrue(flag, "启用失败,请刷新页面");
    }

    /**
     * 此处事务用问题
     *
     * @param dto
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void disEnable(Menu dto) {
        AssertUtil.isNotNull(dto.getId(), "id不能为空");
        List<Menu> list = findDirectChildByPid(dto.getId());
        AssertUtil.isEmpty(list, "存在子菜单，无法禁用");
        dto.setStatus(EnabledEnum.DISENABLED.getValue());
        LambdaQueryWrapper<Menu> updateWrapper = new LambdaQueryWrapper<>();
        updateWrapper.eq(Menu::getId, dto.getId());
        updateWrapper.eq(Menu::getStatus, EnabledEnum.ENABLED.getValue());
        boolean flag = update(dto, updateWrapper);
        AssertUtil.isNotTrue(flag, "禁用失败,请刷新页面");
    }

    @Override
    public void changeState(Menu dto) {
        AssertUtil.isNotNull(dto.getId(), "id不能为空");
        AssertUtil.isNotNull(dto.getStatus(), "状态不能为空");
        if (dto.getStatus() == 0) {
            enable(dto);
        } else {
            disEnable(dto);
        }
    }

    @Override
    public void sort(Menu dto) {
        Menu menu = getById(dto.getId());
        List<Menu> brotherList = findDirectChildByPid(menu.getPid());

    }


    @Override
    public List<Menu> getEnableMenuList(List<Long> idList) {
        LambdaQueryWrapper<Menu> lambdaQueryWrapper = new QueryWrapper<Menu>().lambda()
                .in(Menu::getId, idList)
                .eq(Menu::getStatus, EnabledEnum.ENABLED.getValue());
        return list(lambdaQueryWrapper);
    }


    /**
     * 查询所有可用的菜单
     *
     * @return
     */
    @Override
    public List<Menu> getEnableMenuList() {
        LambdaQueryWrapper<Menu> lambdaQueryWrapper = new QueryWrapper<Menu>().lambda()
                .eq(Menu::getStatus, EnabledEnum.ENABLED.getValue());
        return list(lambdaQueryWrapper);
    }


    @Override
    public Menu getParentMenu(Long pid) {
        Menu parentMenu = getById(pid);
        AssertUtil.isNotNull(parentMenu, "父菜单不存在");
        return parentMenu;
    }

    @Override
    public List<Menu> findDirectChildByPid(Long id) {
        LambdaQueryWrapper<Menu> lambdaQueryWrapper = new QueryWrapper<Menu>().lambda()
                .eq(Menu::getPid, id);
        return list(lambdaQueryWrapper);
    }


    @Override
    public Menu getById(Serializable id) {
        Menu menu = super.getById(id);
        AssertUtil.isNotNull(menu, "该菜单不存在");
        return menu;
    }

    @Override
    public IPage<Menu> queryListByPage(MenuQueryDTO dto) {
        AssertUtil.isNotNull(dto, "参数不能为空");
        IPage<Menu> page = new Page<>(dto.getCurrent(), dto.getSize());
        return page(page);
    }


    @Override
    public void updateByDTO(Menu dto) {
        AssertUtil.isNotNull(dto, "参数不能为空");
        AssertUtil.isNotNull(dto.getId(), "id不能为空");
        boolean flag = super.updateById(dto);
        if (!flag) {
            BusinessException.throwMessage("更新失败,请刷新重试");
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateBatchById(Collection<Menu> entityList) {
        AssertUtil.isNotEmpty(entityList, "参数不能为空");
        boolean flag = updateBatchById(entityList, entityList.size());
        if (!flag) {
            BusinessException.throwMessage("批量更新失败,请刷新重试");
        }
        return flag;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteBatchByDTO(Collection<Menu> entityList) {
        AssertUtil.isNotEmpty(entityList, "参数不能为空");
        boolean flag = deleteByIdsWithFill(entityList);
        if (!flag) {
            BusinessException.throwMessage("批量更新失败,请刷新重试");
        }
    }


    @Override
    public PageVO<MenuTreeVO> getMenuTreeVoList(MenuQueryDTO dto) {
        List<Menu> menuList = list();
        PageVO<MenuTreeVO> pageVO = new PageVO<>();
        pageVO.setRecords(TreeUtil.buildTree(menuList,MenuTreeVO.class));
        pageVO.setTotal((long) pageVO.getRecords().size());
        return pageVO;
    }

    @Override
    public List<MenuTreeLabelVO> getEnabledMenuTreeList() {
        List<Menu> menuList = getEnableMenuList();
        return TreeUtil.buildTree(menuList,MenuTreeLabelVO.class);
    }

    @Override
    public List<MenuTreeLabelVO> getdMenuTreeList() {
        List<Menu> menuList = list();
        return TreeUtil.buildTree(menuList,MenuTreeLabelVO.class);
    }


    @Override
    public List<MenuTreeVO> getMenuTreeVoListByOperator(Long operatorId) {
        List<Menu> menuList = getEnableMenuListByOperatorId(operatorId);
        if (CollectionUtils.isEmpty(menuList)) {
            return new ArrayList<>();
        }
        return TreeUtil.buildTree(menuList,MenuTreeVO.class);
    }

    @Override
    public List<MenuComponentVo> getMenuComponentTreeVoListByOperator(Long operatorId) {
        List<Menu> menuList = getEnableMenuListByOperatorId(operatorId);
        menuList = menuList.stream().filter(menu -> menu.getType() <= 1).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(menuList)) {
            return new ArrayList<>();
        }
        List<MenuTreeVO> menuTreeVoList = TreeUtil.buildTree(menuList,MenuTreeVO.class);
        return ComponentUtils.buildMenuComponents(menuTreeVoList);
    }


    @Override
    public List<Menu> getEnableMenuListByOperatorId(Long operatorId) {
        List<Role> roleList = operatorRoleService.getEnableListByOperatorId(operatorId);
        if (CollectionUtils.isEmpty(roleList)) {
            return new ArrayList<>();
        }
        List<Long> roleIdList = roleList.stream().map(Role::getId).collect(Collectors.toList());
        return roleMenuService.getEnableMenuListByRoleIds(roleIdList);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteBatchByDTO(Long operatorId, List<Long> idList) {
        AssertUtil.isNotEmpty(idList, "集合不能为空");
        Collection<Menu> entityList = new ArrayList<>();
        idList.forEach(id -> {
            Menu menuTemp = getById(id);
            if (menuTemp.getStatus() == EnabledEnum.ENABLED.getValue()) {
                BusinessException.throwMessage("菜单处于启用状态，无法删除.");
            }
            List<Menu> list = findDirectChildByPid(id);
            AssertUtil.isEmpty(list, "存在子菜单，不能删除");
            Menu menu = new Menu();
            menu.setId(id);
            menu.setUpdateId(operatorId);
            entityList.add(menu);
        });
        boolean flag = deleteByIdsWithFill(entityList);
        AssertUtil.isNotTrue(flag, "删除失败,请刷新重试");
    }

}
