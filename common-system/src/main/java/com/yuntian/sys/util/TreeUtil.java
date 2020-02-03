package com.yuntian.sys.util;

import com.yuntian.architecture.util.BeanCopyUtil;
import com.yuntian.sys.model.entity.Menu;
import com.yuntian.sys.model.vo.MenuTreeVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: yuntian
 * @Date: 2020/2/1 0001 14:14
 * @Description:
 */
public class TreeUtil {

    public static List<MenuTreeVO> getMenuTreeVolist(List<Menu> menuList) {
        List<MenuTreeVO> menuTreeVoList = new ArrayList<>();
        for (Menu menu : menuList) {
            MenuTreeVO menuTreeVO = new MenuTreeVO();
            BeanCopyUtil.copyProperties(menu, menuTreeVO);
            menuTreeVoList.add(menuTreeVO);
        }
        setTreeMenu(menuTreeVoList);
        //删除掉所以不为顶级目录的元素
        menuTreeVoList.removeIf(roleAuthVO -> roleAuthVO.getPid() != null && roleAuthVO.getPid() != 0);
        return menuTreeVoList;
    }

    private static void setTreeMenu(List<MenuTreeVO> list) {
        for (MenuTreeVO menuTreeVO : list) {
            for (MenuTreeVO child : list) {
                List<MenuTreeVO> treeVoList;
                if (menuTreeVO.getChildList() == null) {
                    treeVoList = new ArrayList<>();
                    menuTreeVO.setChildList(treeVoList);
                } else {
                    treeVoList = menuTreeVO.getChildList();
                }
                if (menuTreeVO.getId().equals(child.getPid())) {
                    treeVoList.add(child);
                }
            }
        }
    }


}
