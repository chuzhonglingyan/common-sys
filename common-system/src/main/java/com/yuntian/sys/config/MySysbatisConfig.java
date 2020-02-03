package com.yuntian.sys.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author yuntian
 * @Date: 2020/1/29 0029 22:00
 * @Description:
 */
@EnableTransactionManagement
@Configuration
@MapperScan("com.yuntian.sys.mapper*")
public class MySysbatisConfig {
}
