package com.yuntian.sys.common.constant;

/**
 * @author Administrator
 * @Auther: yuntian
 * @Date: 2020/2/27 0027 16:28
 * @Description:
 */
public interface PermissionCodes {

    String USER = "user:";
    String MENU = "menu:";
    String ROLE = "role:";
    String DICT = "dict:";

    /**
     * 用户列表
     */
    String USER_LIST = USER + "list";
    /**
     * 用户新增
     */
    String USER_ADD = USER + "add";
    /**
     * 用户编辑
     */
    String USER_EDIT = USER + "edit";
    /**
     * 用户删除
     */
    String USER_DEL = USER + "del";

    /**
     * 用户修改状态
     */
    String USER_STATE = USER + "state";


    /**
     * 菜单列表
     */
    String MENU_LIST = MENU + "list";
    /**
     * 菜单新增
     */
    String MENU_ADD = MENU + "add";
    /**
     * 菜单编辑
     */
    String MENU_EDIT = MENU + "edit";
    /**
     * 菜单删除
     */
    String MENU_DEL = MENU + "del";

    /**
     * 菜单修改状态
     */
    String MENU_STATE = MENU + "state";

    /**
     * 角色列表
     */
    String ROLE_LIST = ROLE + "list";
    /**
     * 角色新增
     */
    String ROLE_ADD = ROLE + "add";
    /**
     * 角色编辑
     */
    String ROLE_EDIT = ROLE + "edit";
    /**
     * 角色删除
     */
    String ROLE_DEL = ROLE + "del";

    /**
     * 角色修改状态
     */
    String ROLE_STATE = ROLE + "state";

    /**
     * 字典列表
     */
    String DICT_LIST = DICT + "list";
    /**
     * 字典新增
     */
    String DICT_ADD = DICT + "add";
    /**
     * 字典编辑
     */
    String DICT_EDIT = DICT + "edit";
    /**
     * 字典删除
     */
    String DICT_DEL = DICT + "del";


}
