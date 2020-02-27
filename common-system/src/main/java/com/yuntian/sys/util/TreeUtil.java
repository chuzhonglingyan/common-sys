package com.yuntian.sys.util;

import com.yuntian.architecture.util.BeanCopyUtil;
import com.yuntian.sys.common.dict.VisibleEnum;
import com.yuntian.sys.model.entity.Menu;
import com.yuntian.sys.model.vo.MenuComponentVo;
import com.yuntian.sys.model.vo.MenuMetaVo;
import com.yuntian.sys.model.vo.MenuTreeLabelVO;
import com.yuntian.sys.model.vo.MenuTreeVO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

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
                if (menuTreeVO.getChildren() == null) {
                    treeVoList = new ArrayList<>();
                    menuTreeVO.setChildren(treeVoList);
                } else {
                    treeVoList = menuTreeVO.getChildren();
                }
                if (menuTreeVO.getId().equals(child.getPid())) {
                    treeVoList.add(child);
                }
            }
        }
    }

    public static List<MenuTreeVO> buildMenuTree(List<Menu> menuList) {
        List<MenuTreeVO> menuTreeVoList = new ArrayList<>();
        for (Menu menu : menuList) {
            MenuTreeVO menuTreeVO = new MenuTreeVO();
            BeanCopyUtil.copyProperties(menu, menuTreeVO);
            menuTreeVoList.add(menuTreeVO);
        }
        return buildTree(menuTreeVoList);
    }

    public static List<MenuTreeLabelVO> buildMenuLableTree(List<Menu> menuList) {
        List<MenuTreeLabelVO> menuTreeVoList = new ArrayList<>();
        for (Menu menu : menuList) {
            MenuTreeLabelVO menuTreeVO = new MenuTreeLabelVO();
            menuTreeVO.setId(menu.getId());
            menuTreeVO.setPid(menu.getPid());
            menuTreeVO.setLabel(menu.getName());
            menuTreeVoList.add(menuTreeVO);
        }
        return buildLableTree(menuTreeVoList);
    }

    private static List<MenuTreeLabelVO> buildLableTree(List<MenuTreeLabelVO> list) {
        List<MenuTreeLabelVO> trees = new ArrayList<>();
        Set<Long> ids = new HashSet<>();
        for (MenuTreeLabelVO menuTreeVO : list) {
            if (menuTreeVO.getPid() == 0) {
                trees.add(menuTreeVO);
            }
            for (MenuTreeLabelVO it : list) {
                if (it.getPid().equals(menuTreeVO.getId())) {
                    if (menuTreeVO.getChildren() == null) {
                        menuTreeVO.setChildren(new ArrayList<>());
                    }
                    menuTreeVO.getChildren().add(it);
                    ids.add(it.getId());
                }
            }
        }
        if (trees.size() == 0) {
            trees = list.stream().filter(s -> !ids.contains(s.getId())).collect(Collectors.toList());
        }
        return trees;
    }


    public static List<MenuTreeVO> buildTree(List<MenuTreeVO> list) {
        List<MenuTreeVO> trees = new ArrayList<>();
        Set<Long> ids = new HashSet<>();
        for (MenuTreeVO menuTreeVO : list) {
            if (menuTreeVO.getPid() == 0) {
                trees.add(menuTreeVO);
            }
            for (MenuTreeVO it : list) {
                if (it.getPid().equals(menuTreeVO.getId())) {
                    if (menuTreeVO.getChildren() == null) {
                        menuTreeVO.setChildren(new ArrayList<>());
                    }
                    menuTreeVO.getChildren().add(it);
                    ids.add(it.getId());
                }
            }
        }
        if (trees.size() == 0) {
            trees = list.stream().filter(s -> !ids.contains(s.getId())).collect(Collectors.toList());
        }
        return trees;
    }


    public static List<MenuComponentVo> buildMenuComponents(List<MenuTreeVO> menuRootTreeList) {
        List<MenuComponentVo> list = new ArrayList<>();
        menuRootTreeList.forEach(menuTreeVO -> {
                    if (menuTreeVO != null) {
                        List<MenuTreeVO> menuTreeVoList = menuTreeVO.getChildren();
                        MenuComponentVo menuComponentVo = new MenuComponentVo();
                        menuComponentVo.setName(ObjectUtil.isNotEmpty(menuTreeVO.getComponentName()) ? menuTreeVO.getComponentName() : menuTreeVO.getName());
                        // 一级目录需要加斜杠，不然会报警告
                        menuComponentVo.setPath(menuTreeVO.getPid() == 0 ? "/" + menuTreeVO.getPath() : menuTreeVO.getPath());
                        menuComponentVo.setHidden(menuTreeVO.getVisible()== VisibleEnum.HIDDEN.getValue());
                        // 如果不是外链
                        if (menuTreeVO.getIsLinked() == 0) {
                            if (menuTreeVO.getPid() == 0) {
                                menuComponentVo.setComponent(StrUtil.isEmpty(menuTreeVO.getComponent()) ? "Layout" : menuTreeVO.getComponent());
                            } else if (!StrUtil.isEmpty(menuTreeVO.getComponent())) {
                                menuComponentVo.setComponent(menuTreeVO.getComponent());
                            }
                        }
                        menuComponentVo.setMeta(new MenuMetaVo(menuTreeVO.getName(), menuTreeVO.getIcon(),menuTreeVO.getCache()==0));
                        if (menuTreeVoList != null && menuTreeVoList.size()>0) {
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
