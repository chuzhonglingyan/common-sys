package com.yuntian.sys.enums;

/**
 * @author Administrator
 * @Date: 2020/2/1 0001 12:34
 * @Description:
 */
public enum EnabledEnum {

    /**
     * 禁用
     */
    DISENABLED("禁用", 0),
    /**
     * 启用
     */
    ENABLED("启用", 1),
    ;

    private String name;
    private int value;

    EnabledEnum(String name, int value) {
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
