package com.test.sysw;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Administrator
 */
@NacosPropertySource(dataId = "redis", groupId= "sys-web",  autoRefreshed = true)
@NacosPropertySource(dataId = "data-source", groupId= "sys-web",  autoRefreshed = true)
@ComponentScan(basePackages = { "com.yuntian"})
@SpringBootApplication
public class SysWApplication {

    public static void main(String[] args) {
        SpringApplication.run(SysWApplication.class, args);
    }

}
