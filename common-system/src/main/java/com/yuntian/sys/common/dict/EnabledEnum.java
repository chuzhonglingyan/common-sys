package com.yuntian.sys.common.dict;

/**
 * @author Administrator
 * @Date: 2020/2/1 0001 12:34
 * @Description:
 */
public enum EnabledEnum {

    /**
     * 启用
     */
    ENABLED("启用", 0),
    /**
     * 禁用
     */
    DISENABLED("禁用", 1),
    ;

    private String name;
    private int type;

    EnabledEnum(String name, int type) {
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
