package com.yuntian.sys.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yuntian.architecture.data.IBaseService;

import java.util.Collection;
import java.util.List;

import com.yuntian.sys.model.dto.RoleMenuDTO;
import com.yuntian.sys.model.entity.Menu;
import com.yuntian.sys.model.entity.RoleMenu;
import com.yuntian.sys.model.vo.MenuVO;

/**
 * <p>
 * 后台系统-角色菜单关系表 服务类
 * </p>
 *
 * @author yuntian
 * @since 2020-01-31
 */
public interface RoleMenuService extends IBaseService<RoleMenu> {



   void saveMenuListByRoleId(RoleMenuDTO roleMenuDTO);

   void updateByDTO(RoleMenu dto);

   void deleteByDTO(RoleMenu dto);

   void   deleteByRoleId(Long roleId);

   void   deleteByMenuId(Long menuId);

   void deleteBatchByDTO(Collection<RoleMenu> entityList);

   IPage<RoleMenu> queryListByPage(RoleMenuDTO dto);

   List<Long> getMenuIdListByRoleId(Long roleId);

   List<Long> getMenuIdListByRoleId(List<Long> roleIdList);

   List<MenuVO> getMenuListByRoleId(Long roleId);

   List<Menu> getEnableMenuListByRoleId(Long roleId);

   List<Menu> getEnableMenuListByRoleIds(List<Long> roleIdList);

}
