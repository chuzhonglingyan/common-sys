package com.yuntian.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author yuntian
 * @Date: 2020/1/29 0029 22:00
 * @Description:
 */
@EnableTransactionManagement
@Configuration
@MapperScan(value = {"com.yuntian.sys.mapper"})
public class MySysbatisConfig {
}
