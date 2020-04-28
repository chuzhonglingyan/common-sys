package com.yuntian.cache;

import org.apache.commons.codec.binary.Hex;

import java.util.StringJoiner;

/**
 * @Auther: yuntian
 * @Date: 2018/8/21 23:57
 * @Description:
 */
public class SysRedisKey {

    private static final String BACKEND_PREFIX = "sys:";

    private static final String BACKEND_LOGIN_PREFIX = BACKEND_PREFIX + "token:%s";

    private static final String BACKEND_USER_PREFIX = BACKEND_PREFIX + "user:%s";

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

    public static String getOperatorInfoKey(String token, String ip) {
        StringJoiner stringJoiner = new StringJoiner("_", "", "");
        stringJoiner.add(token);
        stringJoiner.add(ip);
        return String.format(BACKEND_USER_PREFIX, Hex.encodeHexString(stringJoiner.toString().getBytes()));
    }

}
