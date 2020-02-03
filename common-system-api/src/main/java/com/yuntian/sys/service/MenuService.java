package com.yuntian.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yuntian.architecture.data.IBaseService;

import java.util.Collection;
import java.util.List;

import com.yuntian.sys.model.dto.MenuQueryDTO;
import com.yuntian.sys.model.dto.MenuSaveDTO;
import com.yuntian.sys.model.dto.MenuUpdateDTO;
import com.yuntian.sys.model.entity.Menu;
import com.yuntian.sys.model.vo.MenuTreeVO;

/**
 * <p>
 * 后台系统-菜单表 服务类
 * </p>
 *
 * @author yuntian
 * @since 2020-01-31
 */
public interface MenuService extends IBaseService<Menu> {


    void saveByDTO(MenuSaveDTO dto);

    void updateByDTO(MenuUpdateDTO dto);


    void updateByDTO(Menu dto);

    void deleteByDTO(Menu dto);

    void isEnable(Menu dto);

    void isDisEnable(Menu dto);

    void sort(Menu dto);

    IPage<Menu> queryListByPage(MenuQueryDTO dto);

    void deleteBatchByDTO(Collection<Menu> entityList);

    List<Menu> getEnableMenuList();


    List<Menu> getEnableMenuList(List<Long> idList);

    List<MenuTreeVO> getMenuTreeVoList();

}
