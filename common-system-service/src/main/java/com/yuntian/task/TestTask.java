package com.yuntian.task;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @Auther: yuntian
 * @Date: 2019/3/17 0017 14:29
 * @Description:
 */
@Component("testTask")
@Slf4j
public class TestTask {


    public void test() {
        log.info("测试定时器");
    }

    public void test(String text) {
        log.info("测试定时器:" + text);
    }
}
