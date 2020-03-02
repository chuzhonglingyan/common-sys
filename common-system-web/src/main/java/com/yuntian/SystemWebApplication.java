package com.yuntian;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@NacosPropertySource(dataId = "redis", groupId= "sys-web",  autoRefreshed = true)
@NacosPropertySource(dataId = "data-source", groupId= "sys-web",  autoRefreshed = true)
@SpringBootApplication
public class SystemWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SystemWebApplication.class, args);
    }

}
