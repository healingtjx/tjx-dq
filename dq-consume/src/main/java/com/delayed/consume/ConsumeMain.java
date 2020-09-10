package com.delayed.consume;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @作者: tjx
 * @描述: 消费者启动类
 * @创建时间: 创建于14:26 2020/9/9
 **/
@ComponentScan(basePackages = "com.delayed")
@SpringBootApplication
public class ConsumeMain {

    public static void main(String[] args) {
        SpringApplication.run(ConsumeMain.class,args);
    }
}
