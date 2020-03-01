package com.yuntian.sys;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Administrator
 */
@NacosPropertySource(dataId = "redis", groupId= "sys-web",  autoRefreshed = true)
@NacosPropertySource(dataId = "data-source", groupId= "sys-web",  autoRefreshed = true)
@SpringBootApplication
public class SysApplication {

    public static void main(String[] args) {
        SpringApplication.run(SysApplication.class, args);
    }

}
