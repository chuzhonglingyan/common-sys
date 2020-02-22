package com.yuntian.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuntian.architecture.data.BaseServiceImpl;
import com.yuntian.architecture.data.exception.BusinessException;
import com.yuntian.architecture.data.util.AssertUtil;
import com.yuntian.architecture.util.BeanCopyUtil;
import com.yuntian.sys.common.dict.EnabledEnum;
import com.yuntian.sys.common.dict.MenuTypeEnum;
import com.yuntian.sys.mapper.MenuMapper;
import com.yuntian.sys.model.dto.MenuQueryDTO;
import com.yuntian.sys.model.dto.MenuSaveDTO;
import com.yuntian.sys.model.dto.MenuUpdateDTO;
import com.yuntian.sys.model.entity.Menu;
import com.yuntian.sys.model.vo.MenuComponentVo;
import com.yuntian.sys.model.vo.MenuMetaVo;
import com.yuntian.sys.model.vo.MenuTreeVO;
import com.yuntian.sys.service.MenuService;
import com.yuntian.sys.service.RoleMenuService;
import com.yuntian.sys.util.TreeUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import static com.yuntian.sys.common.constant.Constants.ONE;


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

    /**
     * 保存菜单
     *
     * @param dto
     */
    @Override
    public void saveByDTO(MenuSaveDTO dto) {
        Menu menu = BeanCopyUtil.copyProperties(dto, Menu.class);
        if (dto.getPid() == 0) {
            dto.setLevel(ONE);
            dto.setType(MenuTypeEnum.ROOT.getType());
        } else {
            Menu parentMenu = getParentMenu(dto.getPid());
            if (parentMenu.getType() == MenuTypeEnum.BUTTON.getType()) {
                BusinessException.throwMessage("操作类型菜单不能添加子级菜单");
            }
            dto.setLevel(parentMenu.getLevel() + 1);
            List<Menu> brotherList = findDirectChildByPid(dto.getPid());
            Integer sort = brotherList == null ? 1 : brotherList.size() + 1;
            dto.setSort(sort);
        }
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
        if (menu.getStatus() == EnabledEnum.DISENABLED.getType()) {
            BusinessException.throwMessage("菜单处于冻结状态，无法删除.");
        }
        List<Menu> list = findDirectChildByPid(dto.getId());
        AssertUtil.isEmpty(list, "存在子菜单，不能删除");
        boolean flag = deleteByIdWithFill(dto);
        AssertUtil.isNotTrue(flag, "删除失败,请刷新重试");
        roleMenuService.deleteByMenuId(dto.getId());
    }


    @Override
    public void isEnable(Menu dto) {
        dto.setStatus(EnabledEnum.DISENABLED.getType());
        UpdateWrapper<Menu> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("id", dto.getId());
        updateWrapper.set("status", EnabledEnum.ENABLED.getType());
        update(dto, updateWrapper);
    }


    @Override
    public void isDisEnable(Menu dto) {
        dto.setStatus(EnabledEnum.ENABLED.getType());
        UpdateWrapper<Menu> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("id", dto.getId());
        updateWrapper.set("status", EnabledEnum.DISENABLED.getType());
        update(dto, updateWrapper);
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
                .eq(Menu::getStatus, EnabledEnum.ENABLED.getType());
        return list(lambdaQueryWrapper);
    }


    /**
     * 查询所有可用的菜单
     *
     * @return
     */
    @Override
    public List<Menu> getEnableMenuList() {
        Map<String, Object> map = new HashMap<>();
        //列名条件
        map.put("status", EnabledEnum.ENABLED.getType());
        return super.listByMap(map);
    }


    public Menu getParentMenu(Long pid) {
        Menu parentMenu = super.getById(pid);
        AssertUtil.isNotNull(parentMenu, "父菜单不存在");
        return parentMenu;
    }

    public List<Menu> findDirectChildByPid(Long pid) {
        Menu parentMenu = getParentMenu(pid);
        Map<String, Object> map = new HashMap<>();
        //列名条件
        map.put("pid", parentMenu.getPid());
        return super.listByMap(map);
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
    public List<MenuTreeVO> getMenuTreeVoList() {
        List<Menu> menuList = list();
        if (CollectionUtils.isEmpty(menuList)) {
            return new ArrayList<>();
        }
        return TreeUtil.getMenuTreeVolist(menuList);
    }



}
