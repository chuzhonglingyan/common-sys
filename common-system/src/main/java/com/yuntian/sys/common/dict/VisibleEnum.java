package com.yuntian.sys.common.dict;

/**
 * @author Administrator
 * @Date: 2020/2/1 0001 12:34
 * @Description:
 */
public enum VisibleEnum {

    /**
     * 隐藏
     */
    HIDDEN("隐藏", 0),
    /**
     * 显示
     */
    VISIBLE("显示", 1),
    ;

    private String name;
    private int value;

    VisibleEnum(String name, int value) {
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
