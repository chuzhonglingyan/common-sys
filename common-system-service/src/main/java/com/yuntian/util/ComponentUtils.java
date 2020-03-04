package com.yuntian.util;

import com.yuntian.sys.enums.VisibleEnum;
import com.yuntian.sys.model.vo.MenuComponentVo;
import com.yuntian.sys.model.vo.MenuMetaVo;
import com.yuntian.sys.model.vo.MenuTreeVO;

import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * @author yuntian
 * @date 2020/3/4 0004 23:00
 * @description
 */
public class ComponentUtils {


    public static List<MenuComponentVo> buildMenuComponents(List<MenuTreeVO> menuRootTreeList) {
        List<MenuComponentVo> list = new ArrayList<>();
        menuRootTreeList.forEach(menuTreeVO -> {
                    if (menuTreeVO != null) {
                        List<MenuTreeVO> menuTreeVoList = menuTreeVO.getChildren();
                        MenuComponentVo menuComponentVo = new MenuComponentVo();
                        menuComponentVo.setName(ObjectUtil.isNotEmpty(menuTreeVO.getComponentName()) ? menuTreeVO.getComponentName() : menuTreeVO.getName());
                        // 一级目录需要加斜杠，不然会报警告
                        menuComponentVo.setPath(menuTreeVO.getPid() == 0 ? "/" + menuTreeVO.getPath() : menuTreeVO.getPath());
                        menuComponentVo.setHidden(menuTreeVO.getVisible() == VisibleEnum.HIDDEN.getValue());
                        // 如果不是外链
                        if (menuTreeVO.getIsLinked() == 0) {
                            if (menuTreeVO.getPid() == 0) {
                                menuComponentVo.setComponent(StrUtil.isEmpty(menuTreeVO.getComponent()) ? "Layout" : menuTreeVO.getComponent());
                            } else if (!StrUtil.isEmpty(menuTreeVO.getComponent())) {
                                menuComponentVo.setComponent(menuTreeVO.getComponent());
                            }
                        }
                        menuComponentVo.setMeta(new MenuMetaVo(menuTreeVO.getName(), menuTreeVO.getIcon(), menuTreeVO.getCache() == 0));
                        if (menuTreeVoList != null && menuTreeVoList.size() > 0) {
                            menuComponentVo.setAlwaysShow(true);
                            menuComponentVo.setRedirect("noredirect");
                            menuComponentVo.setChildren(buildMenuComponents(menuTreeVoList));
                            // 处理是一级菜单并且没有子菜单的情况
                        } else if (menuTreeVO.getPid() == 0) {
                            MenuComponentVo menuComponentVo1 = new MenuComponentVo();
                            menuComponentVo1.setMeta(menuComponentVo.getMeta());
                            // 非外链
                            if (menuTreeVO.getIsLinked() == 0) {
                                menuComponentVo1.setPath("index");
                                menuComponentVo1.setName(menuComponentVo.getName());
                                menuComponentVo1.setComponent(menuComponentVo.getComponent());
                            } else {
                                menuComponentVo1.setPath(menuTreeVO.getPath());
                            }
                            menuComponentVo.setName(null);
                            menuComponentVo.setMeta(null);
                            menuComponentVo.setComponent("Layout");
                            List<MenuComponentVo> list1 = new ArrayList<>();
                            list1.add(menuComponentVo1);
                            menuComponentVo.setChildren(list1);
                        }
                        list.add(menuComponentVo);
                    }
                }
        );
        return list;
    }
}
