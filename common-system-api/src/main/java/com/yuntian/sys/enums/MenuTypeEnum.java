package com.yuntian.sys.enums;

/**
 * @author Administrator
 * @Date: 2020/2/1 0001 12:34
 * @Description:
 */
public enum MenuTypeEnum {

    /**
     * 根目录
     */
    ROOT("根目录", 0),
    /**
     * 菜单
     */
    MENU("菜单", 1),
    /**
     * 按钮
     */
    BUTTON("按钮", 2);

    private String name;
    private int value;

    MenuTypeEnum(String name, int value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value + "_" + this.name;
    }

    public int getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }

}
