package com.yuntian.sysw;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Administrator
 */
@NacosPropertySource(dataId = "redis", groupId= "sys-web",  autoRefreshed = true)
@NacosPropertySource(dataId = "data-source", groupId= "sys-web",  autoRefreshed = true)
@SpringBootApplication
public class SysWApplication {

    public static void main(String[] args) {
        SpringApplication.run(SysWApplication.class, args);
    }

}
