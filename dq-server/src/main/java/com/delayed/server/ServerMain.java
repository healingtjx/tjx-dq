package com.delayed.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/**
 * @作者: tjx
 * @描述: server 服务启动类
 * @创建时间: 创建于15:11 2020/7/14
 **/
@ComponentScan(basePackages = "com.delayed")
@SpringBootApplication
public class ServerMain extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 注意这里要指向原先用main方法执行的Application启动类
        return builder.sources(ServerMain.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(ServerMain.class,args);
    }

}
