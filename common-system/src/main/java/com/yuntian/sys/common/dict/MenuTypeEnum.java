package com.yuntian.sys.common.dict;

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
    private int type;

    MenuTypeEnum(String name, int type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type + "_" + this.name;
    }

    public int getType(){
      return  this.type;
    }

}
