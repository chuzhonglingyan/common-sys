package com.yuntian.util;


import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: yuntian
 * @Date: 2020/2/1 0001 12:04
 * @Description:
 */
public class BeanCopyUtil {


    public static <F, T> void copyProperties(F source, T target) {
        BeanUtils.copyProperties(source, target);
    }

    public static <F, T> T copyProperties(F source, Class<T> targetClass) {
        T target = null;
        try {
            target = targetClass.newInstance();
            copyProperties(source, target);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return target;
    }


    public static <F, T> List<T> copyListProperties(List<F> sources, Class<T> targetClass) {
        List<T> list = new ArrayList<>(sources.size());
        for (F source : sources) {
            list.add(copyProperties(source, targetClass));
        }
        return list;
    }


}
