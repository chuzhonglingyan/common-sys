package com.yuntian.sysw;

import com.alibaba.fastjson.JSON;
import com.yuntian.sys.mapper.OperatorMapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(classes = SysApplication.class)
class UserMapperTests {

    @Resource
    private OperatorMapper operatorMapper;

    @Test
    void selectById() {
       log.info(JSON.toJSONString(operatorMapper.selectById(1)));
    }

}
