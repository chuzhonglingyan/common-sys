package com.yuntian.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yuntian.architecture.data.IBaseService;
import com.yuntian.sys.model.dto.MenuQueryDTO;
import com.yuntian.sys.model.dto.MenuSaveDTO;
import com.yuntian.sys.model.dto.MenuUpdateDTO;
import com.yuntian.sys.model.entity.Menu;
import com.yuntian.sys.model.vo.MenuComponentVo;
import com.yuntian.sys.model.vo.MenuTreeLabelVO;
import com.yuntian.sys.model.vo.MenuTreeVO;
import com.yuntian.sys.model.vo.PageVO;

import java.util.Collection;
import java.util.List;

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

    void enable(Menu dto);

    void disEnable(Menu dto);

    void changeState(Menu dto);

    void sort(Menu dto);

    IPage<Menu> queryListByPage(MenuQueryDTO dto);

    void deleteBatchByDTO(Collection<Menu> entityList);

    List<Menu> getEnableMenuList();


    List<Menu> getEnableMenuList(List<Long> idList);

    PageVO<MenuTreeVO> getMenuTreeVoList(MenuQueryDTO dto);

    List<MenuTreeLabelVO> getEnabledMenuTreeList();

    List<MenuTreeLabelVO> getdMenuTreeList();

    Menu getParentMenu(Long id);

    List<Menu> findDirectChildByPid(Long id);



    List<Menu> getEnableMenuListByOperatorId(Long operatorId);

    List<MenuTreeVO> getMenuTreeVoListByOperator(Long operatorId);


    List<MenuComponentVo> getMenuComponentTreeVoListByOperator(Long operatorId);

    void deleteBatchByDTO(Long operatorId, List<Long> idList);

}
