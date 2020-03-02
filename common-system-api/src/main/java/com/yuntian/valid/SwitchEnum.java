package com.yuntian.valid;

/**
 * @Auther: yuntian
 * @Date: 2020/2/25 0025 23:21
 * @Description:
 */
public enum SwitchEnum {

    /**
     * 0
     */
    ZERO(0),
    /**
     * 1
     */
    ONE(1);

    private Integer code;

    SwitchEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static boolean isInEnum(Integer code) {
        for (SwitchEnum gender : SwitchEnum.values()) {
            if (gender.getCode().equals(code)) {
                return true;
            }
        }
        return false;
    }

}
