package com.yuntian.sys.common.constant;

/**
 * @Auther: yuntian
 * @Date: 2018/8/21 23:57
 * @Description:
 */
public class RedisKey {

    private static final String BACKEND_PREFIX = "sys:";

    private static final String BACKEND_LOGIN_PREFIX = "sys:token:%s";


    private static final String TOKEN_PREFIX = "token_%s";


    /**
     * 1天
     */
    public static final int ONE_DAY = 3600 * 24;

    /**
     * 7天
     */
    public static final int ONE_WEEK = 3600 * 24 * 7;


    public static String getBackendTokenkey(String id) {
        return String.format(BACKEND_LOGIN_PREFIX, id);
    }


    public static String getTokenKey(String id) {
        return String.format(TOKEN_PREFIX, id);
    }

}
