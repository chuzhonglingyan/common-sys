package com.yuntian.sysw;

import com.alibaba.fastjson.JSON;
import com.yuntian.sys.service.DictService;
import com.yuntian.sys.service.impl.CacheService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * @Auther: yuntian
 * @Date: 2020/3/1 0001 19:44
 * @Description:
 */
@Slf4j
@SpringBootTest(classes = SysApplication.class)
public class CacheApplicationTests {

    @Resource
    private CacheService cacheService;

    @Resource
    private DictService dictService;

    @Test
    void contextLoads() {
        log.info(cacheService.getName(1L));
        log.info(cacheService.getName(1L));
        cacheService.updateName(1L);
        log.info(cacheService.getName(1L));
        cacheService.deleteName(1L);
        log.info(cacheService.getName(1L));
    }

    @Test
    void getDictAll() {
        log.info(JSON.toJSONString(dictService.getAll()));
    }

}