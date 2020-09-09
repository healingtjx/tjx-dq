package com.delayed.timer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @作者: tjx
 * @描述:
 * @创建时间: 创建于14:03 2020/7/29
 **/
@ComponentScan(basePackages = "com.delayed")
@SpringBootApplication
public class TimerMain {

    public static void main(String[] args) {
        SpringApplication.run(TimerMain.class,args);
    }
}
